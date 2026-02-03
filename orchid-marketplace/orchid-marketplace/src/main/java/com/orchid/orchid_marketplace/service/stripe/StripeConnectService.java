package com.orchid.orchid_marketplace.service.stripe;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.config.StripeProperties;
import com.orchid.orchid_marketplace.dto.StripeConnectAuthorizeResponse;
import com.orchid.orchid_marketplace.dto.StripeConnectCallbackResponse;
import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.model.StripeConnectState;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.StoreRepository;
import com.orchid.orchid_marketplace.repository.StripeConnectStateRepository;
import com.orchid.orchid_marketplace.repository.UserRepository;

@Service
@Profile("!cosmos")
public class StripeConnectService {

    private static final Duration STATE_TTL = Duration.ofMinutes(30);

    private final StripeProperties stripeProperties;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StripeConnectStateRepository stateRepository;
    private final StripeOAuthClient stripeOAuthClient;

    public StripeConnectService(
        StripeProperties stripeProperties,
        StoreRepository storeRepository,
        UserRepository userRepository,
        StripeConnectStateRepository stateRepository,
        StripeOAuthClient stripeOAuthClient
    ) {
        this.stripeProperties = stripeProperties;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.stateRepository = stateRepository;
        this.stripeOAuthClient = stripeOAuthClient;
    }

    @Transactional
    public StripeConnectAuthorizeResponse createAuthorizeUrl(UUID storeId, String returnUrl) {
        Objects.requireNonNull(storeId, "storeId must not be null");

        String clientId = stripeProperties.getConnectClientId();
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalStateException("Stripe Connect client ID is not configured (STRIPE_CONNECT_CLIENT_ID)");
        }

        String redirectUri = stripeProperties.getConnectRedirectUri();
        if (redirectUri == null || redirectUri.isBlank()) {
            throw new IllegalStateException("Stripe Connect redirect URI is not configured (STRIPE_CONNECT_REDIRECT_URI)");
        }

        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found with ID: " + storeId));

        StripeConnectState state = new StripeConnectState();
        state.setStore(store);
        state.setReturnUrl(returnUrl);
        stateRepository.save(state);

        String url = buildAuthorizeUrl(clientId, redirectUri, state.getId());

        StripeConnectAuthorizeResponse resp = new StripeConnectAuthorizeResponse();
        resp.setUrl(url);
        resp.setState(state.getId());
        resp.setExpiresAt(LocalDateTime.now().plus(STATE_TTL));
        return resp;
    }

    @Transactional
    public StripeConnectCallbackResponse handleCallback(String code, UUID stateId) {
        Objects.requireNonNull(stateId, "state must not be null");
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }

        StripeConnectState state = stateRepository.findById(stateId)
            .orElseThrow(() -> new RuntimeException("Invalid state"));

        if (state.isConsumed()) {
            throw new IllegalStateException("State already consumed");
        }

        if (state.getCreatedAt() != null) {
            LocalDateTime expiresAt = state.getCreatedAt().plus(STATE_TTL);
            if (LocalDateTime.now().isAfter(expiresAt)) {
                throw new IllegalStateException("State expired");
            }
        }

        String stripeAccountId = stripeOAuthClient.exchangeAuthorizationCodeForAccountId(code);

        Store store = state.getStore();
        User seller = store == null ? null : store.getSeller();
        if (seller == null) {
            throw new RuntimeException("Store has no seller");
        }

        seller.setStripeConnectAccountId(stripeAccountId);
        userRepository.save(seller);

        state.setConsumedAt(LocalDateTime.now());
        stateRepository.save(state);

        StripeConnectCallbackResponse resp = new StripeConnectCallbackResponse();
        resp.setStripeAccountId(stripeAccountId);
        return resp;
    }

    @Transactional(readOnly = true)
    public boolean isStoreConnected(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return storeRepository.findById(storeId)
            .map(Store::getSeller)
            .map(User::getStripeConnectAccountId)
            .filter(v -> !v.isBlank())
            .isPresent();
    }

    static String buildAuthorizeUrl(String clientId, String redirectUri, UUID state) {
        StringBuilder sb = new StringBuilder("https://connect.stripe.com/oauth/authorize");
        sb.append("?response_type=code");
        sb.append("&client_id=").append(url(clientId));
        sb.append("&scope=read_write");
        sb.append("&redirect_uri=").append(url(redirectUri));
        sb.append("&state=").append(url(state.toString()));
        return sb.toString();
    }

    private static String url(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }
}
