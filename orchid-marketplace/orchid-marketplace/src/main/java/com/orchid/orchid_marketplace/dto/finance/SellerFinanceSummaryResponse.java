package com.orchid.orchid_marketplace.dto.finance;

import java.math.BigDecimal;

public class SellerFinanceSummaryResponse {
    private BigDecimal grossSalesSubtotal;
    private BigDecimal shippingCollected;
    private BigDecimal platformFees;
    private BigDecimal listingFeesAccrued;
    private BigDecimal listingFeesSettled;
    private BigDecimal taxCollected;

    private BigDecimal netAffectingSellerBalance;

    public BigDecimal getGrossSalesSubtotal() { return grossSalesSubtotal; }
    public void setGrossSalesSubtotal(BigDecimal grossSalesSubtotal) { this.grossSalesSubtotal = grossSalesSubtotal; }

    public BigDecimal getShippingCollected() { return shippingCollected; }
    public void setShippingCollected(BigDecimal shippingCollected) { this.shippingCollected = shippingCollected; }

    public BigDecimal getPlatformFees() { return platformFees; }
    public void setPlatformFees(BigDecimal platformFees) { this.platformFees = platformFees; }

    public BigDecimal getListingFeesAccrued() { return listingFeesAccrued; }
    public void setListingFeesAccrued(BigDecimal listingFeesAccrued) { this.listingFeesAccrued = listingFeesAccrued; }

    public BigDecimal getListingFeesSettled() { return listingFeesSettled; }
    public void setListingFeesSettled(BigDecimal listingFeesSettled) { this.listingFeesSettled = listingFeesSettled; }

    public BigDecimal getTaxCollected() { return taxCollected; }
    public void setTaxCollected(BigDecimal taxCollected) { this.taxCollected = taxCollected; }

    public BigDecimal getNetAffectingSellerBalance() { return netAffectingSellerBalance; }
    public void setNetAffectingSellerBalance(BigDecimal netAffectingSellerBalance) { this.netAffectingSellerBalance = netAffectingSellerBalance; }
}
