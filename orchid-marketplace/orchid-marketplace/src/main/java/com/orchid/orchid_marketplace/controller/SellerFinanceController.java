package com.orchid.orchid_marketplace.controller;

import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.finance.SellerFinanceEntryResponse;
import com.orchid.orchid_marketplace.dto.finance.SellerFinanceSummaryResponse;
import com.orchid.orchid_marketplace.model.SellerLedgerEntry;
import com.orchid.orchid_marketplace.model.SellerLedgerEntryType;
import com.orchid.orchid_marketplace.service.SellerLedgerService;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/seller-finance")
public class SellerFinanceController {

    @Autowired
    private SellerLedgerService sellerLedgerService;

    @GetMapping("/{storeId}/entries")
    public List<SellerFinanceEntryResponse> entries(
        @PathVariable UUID storeId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        List<SellerLedgerEntry> entries = sellerLedgerService.getLedgerEntries(storeId, from, to);
        return entries.stream().map(SellerFinanceController::toResponse).toList();
    }

    @GetMapping("/{storeId}/summary")
    public ResponseEntity<SellerFinanceSummaryResponse> summary(
        @PathVariable UUID storeId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        SellerFinanceSummaryResponse r = new SellerFinanceSummaryResponse();
        r.setGrossSalesSubtotal(sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.SALE_SUBTOTAL, from, to));
        r.setShippingCollected(sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.SHIPPING_COLLECTED, from, to));
        r.setPlatformFees(sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.PLATFORM_FEE, from, to));
        r.setListingFeesAccrued(sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.LISTING_FEE_ACCRUED, from, to));
        r.setListingFeesSettled(sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.LISTING_FEE_SETTLED, from, to));
        r.setTaxCollected(sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.TAX_COLLECTED, from, to));

        // Net affecting seller is just sum of affecting entries.
        r.setNetAffectingSellerBalance(
            sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.SALE_SUBTOTAL, from, to)
                .add(sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.SHIPPING_COLLECTED, from, to))
                .add(sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.PLATFORM_FEE, from, to))
                .add(sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.LISTING_FEE_ACCRUED, from, to))
                .add(sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.LISTING_FEE_SETTLED, from, to))
        );

        return ResponseEntity.ok(r);
    }

    private static SellerFinanceEntryResponse toResponse(SellerLedgerEntry e) {
        SellerFinanceEntryResponse r = new SellerFinanceEntryResponse();
        r.setId(e.getId());
        r.setCreatedAt(e.getCreatedAt());
        r.setType(e.getType());
        r.setAmount(e.getAmount());
        r.setCurrency(e.getCurrency());
        r.setAffectsSellerBalance(e.getAffectsSellerBalance());
        r.setOrderId(e.getOrderId());
        r.setOrderItemId(e.getOrderItemId());
        r.setProductId(e.getProductId());
        r.setIsSettled(e.getIsSettled());
        r.setSettledAt(e.getSettledAt());
        r.setSettledOrderId(e.getSettledOrderId());
        return r;
    }
}
