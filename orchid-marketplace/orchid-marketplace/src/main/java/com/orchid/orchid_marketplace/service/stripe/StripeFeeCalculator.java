package com.orchid.orchid_marketplace.service.stripe;

import java.math.BigDecimal;
import java.util.Objects;

import com.orchid.orchid_marketplace.util.MoneyUtil;

public final class StripeFeeCalculator {
    private StripeFeeCalculator() {}

    public static StripeFeeBreakdown calculate(long itemSubtotalCents, long shippingCents, BigDecimal platformFeePercent, long outstandingListingFeeCents) {
        Objects.requireNonNull(platformFeePercent, "platformFeePercent must not be null");

        long platformFeeCents = MoneyUtil.percentOfCents(itemSubtotalCents, platformFeePercent);

        // We do not take a percentage of tax; and by default we also avoid taking a % of shipping.
        // Listing fees are recovered from future sales by adding them to application_fee_amount.
        long maxAdditional = Math.max(0L, (itemSubtotalCents + shippingCents) - platformFeeCents);
        long listingFeeAppliedCents = Math.min(outstandingListingFeeCents, maxAdditional);

        return new StripeFeeBreakdown(platformFeeCents, listingFeeAppliedCents);
    }
}
