package com.orchid.orchid_marketplace.dto;

public class StripeCheckoutSessionResponse {
    private String sessionId;
    private String url;

    private long platformFeeCents;
    private long listingFeeAppliedCents;

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public long getPlatformFeeCents() { return platformFeeCents; }
    public void setPlatformFeeCents(long platformFeeCents) { this.platformFeeCents = platformFeeCents; }

    public long getListingFeeAppliedCents() { return listingFeeAppliedCents; }
    public void setListingFeeAppliedCents(long listingFeeAppliedCents) { this.listingFeeAppliedCents = listingFeeAppliedCents; }
}
