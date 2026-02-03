package com.orchid.orchid_marketplace.service.stripe;

public class StripeFeeBreakdown {
    private final long platformFeeCents;
    private final long listingFeeAppliedCents;

    public StripeFeeBreakdown(long platformFeeCents, long listingFeeAppliedCents) {
        this.platformFeeCents = platformFeeCents;
        this.listingFeeAppliedCents = listingFeeAppliedCents;
    }

    public long getPlatformFeeCents() { return platformFeeCents; }
    public long getListingFeeAppliedCents() { return listingFeeAppliedCents; }

    public long getApplicationFeeCents() { return platformFeeCents + listingFeeAppliedCents; }
}
