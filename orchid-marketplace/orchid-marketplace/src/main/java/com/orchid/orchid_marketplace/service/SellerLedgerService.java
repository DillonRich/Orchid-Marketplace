package com.orchid.orchid_marketplace.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.model.SellerLedgerEntry;
import com.orchid.orchid_marketplace.model.SellerLedgerEntryType;
import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.repository.SellerLedgerEntryRepository;
import com.orchid.orchid_marketplace.repository.StoreRepository;

@Service
@Profile("!cosmos")
public class SellerLedgerService {

    public static final BigDecimal DEFAULT_LISTING_FEE = BigDecimal.valueOf(-0.25);

    @Autowired
    private SellerLedgerEntryRepository sellerLedgerEntryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Transactional
    public void accrueListingFeeForProduct(Product product) {
        Objects.requireNonNull(product, "product must not be null");
        Store store = product.getStore();
        Objects.requireNonNull(store, "product.store must not be null");

        SellerLedgerEntry e = new SellerLedgerEntry();
        e.setStore(store);
        e.setType(SellerLedgerEntryType.LISTING_FEE_ACCRUED);
        e.setAmount(DEFAULT_LISTING_FEE);
        e.setAffectsSellerBalance(true);
        e.setProductId(product.getId());
        e.setIsSettled(false);

        sellerLedgerEntryRepository.save(e);
    }

    public long countOutstandingListingFees(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return sellerLedgerEntryRepository.countByStoreIdAndTypeAndIsSettledFalse(storeId, SellerLedgerEntryType.LISTING_FEE_ACCRUED);
    }

    public List<SellerLedgerEntry> getLedgerEntries(UUID storeId, LocalDateTime from, LocalDateTime to) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        Objects.requireNonNull(from, "from must not be null");
        Objects.requireNonNull(to, "to must not be null");
        return sellerLedgerEntryRepository.findByStoreIdAndCreatedAtBetweenOrderByCreatedAtDesc(storeId, from, to);
    }

    public BigDecimal sumByType(UUID storeId, SellerLedgerEntryType type, LocalDateTime from, LocalDateTime to) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(from, "from must not be null");
        Objects.requireNonNull(to, "to must not be null");
        return sellerLedgerEntryRepository.sumByTypeBetween(storeId, type, from, to);
    }

    @Transactional
    public long settleOldestListingFees(UUID storeId, long listingFeeCentsToSettle, UUID settledOrderId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        Objects.requireNonNull(settledOrderId, "settledOrderId must not be null");
        if (listingFeeCentsToSettle <= 0) return 0;

        // Each listing fee entry is -$0.25.
        long maxCount = listingFeeCentsToSettle / 25;
        if (maxCount <= 0) return 0;

        List<SellerLedgerEntry> outstanding = sellerLedgerEntryRepository
            .findByStoreIdAndTypeAndIsSettledFalseOrderByCreatedAtAsc(storeId, SellerLedgerEntryType.LISTING_FEE_ACCRUED);

        long settledCount = 0;
        LocalDateTime now = LocalDateTime.now();
        java.util.ArrayList<SellerLedgerEntry> toSave = new java.util.ArrayList<>();
        for (SellerLedgerEntry e : outstanding) {
            if (settledCount >= maxCount) break;
            e.setIsSettled(true);
            e.setSettledAt(now);
            e.setSettledOrderId(settledOrderId);
            toSave.add(e);
            settledCount++;
        }

        if (!toSave.isEmpty()) {
            sellerLedgerEntryRepository.saveAll(toSave);
        }

        // Record a separate informational entry that the listing fees were settled against an order.
        SellerLedgerEntry settled = new SellerLedgerEntry();
        settled.setStore(storeRepository.getReferenceById(storeId));
        settled.setType(SellerLedgerEntryType.LISTING_FEE_SETTLED);
        settled.setAmount(BigDecimal.valueOf(-0.25).multiply(BigDecimal.valueOf(settledCount)));
        settled.setAffectsSellerBalance(true);
        settled.setOrderId(settledOrderId);
        settled.setIsSettled(true);
        settled.setSettledAt(now);
        settled.setSettledOrderId(settledOrderId);

        sellerLedgerEntryRepository.save(settled);

        return settledCount;
    }
}
