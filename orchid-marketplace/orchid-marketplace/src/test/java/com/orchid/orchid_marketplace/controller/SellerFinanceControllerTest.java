package com.orchid.orchid_marketplace.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.orchid.orchid_marketplace.dto.finance.SellerFinanceSummaryResponse;
import com.orchid.orchid_marketplace.model.SellerLedgerEntryType;
import com.orchid.orchid_marketplace.service.SellerLedgerService;

class SellerFinanceControllerTest {

    private SellerFinanceController controller;

    @Mock
    private SellerLedgerService sellerLedgerService;

    private UUID storeId;
    private LocalDateTime from;
    private LocalDateTime to;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new SellerFinanceController();
        ReflectionTestUtils.setField(controller, "sellerLedgerService", sellerLedgerService);

        storeId = UUID.randomUUID();
        from = LocalDateTime.now().minusDays(30);
        to = LocalDateTime.now();
    }

    // ========== entries Tests ==========

    @Test
    void testEntries_Success() {
        when(sellerLedgerService.getLedgerEntries(eq(storeId), eq(from), eq(to))).thenReturn(List.of());

        List result = controller.entries(storeId, from, to);

        assertNotNull(result);
    }

    @Test
    void testEntries_Empty() {
        when(sellerLedgerService.getLedgerEntries(eq(storeId), eq(from), eq(to))).thenReturn(List.of());

        List result = controller.entries(storeId, from, to);

        assertTrue(result.isEmpty());
    }

    @Test
    void testEntries_VerifyServiceCalled() {
        when(sellerLedgerService.getLedgerEntries(eq(storeId), eq(from), eq(to))).thenReturn(List.of());

        controller.entries(storeId, from, to);

        verify(sellerLedgerService).getLedgerEntries(eq(storeId), eq(from), eq(to));
    }

    // ========== summary Tests ==========

    @Test
    void testSummary_Success() {
        when(sellerLedgerService.sumByType(eq(storeId), any(SellerLedgerEntryType.class), eq(from), eq(to)))
            .thenReturn(BigDecimal.ZERO);

        ResponseEntity<SellerFinanceSummaryResponse> result = controller.summary(storeId, from, to);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testSummary_WithData() {
        when(sellerLedgerService.sumByType(eq(storeId), any(SellerLedgerEntryType.class), eq(from), eq(to)))
            .thenReturn(BigDecimal.valueOf(100.00));

        ResponseEntity<SellerFinanceSummaryResponse> result = controller.summary(storeId, from, to);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testSummary_VerifyServiceCalledMultipleTimes() {
        when(sellerLedgerService.sumByType(eq(storeId), any(SellerLedgerEntryType.class), eq(from), eq(to)))
            .thenReturn(BigDecimal.ZERO);

        controller.summary(storeId, from, to);

        verify(sellerLedgerService, times(11)).sumByType(eq(storeId), any(SellerLedgerEntryType.class), eq(from), eq(to));
    }

    @Test
    void testSummary_DifferentDateRanges() {
        LocalDateTime anotherFrom = LocalDateTime.now().minusDays(60);
        LocalDateTime anotherTo = LocalDateTime.now().minusDays(30);

        when(sellerLedgerService.sumByType(eq(storeId), any(SellerLedgerEntryType.class), any(), any()))
            .thenReturn(BigDecimal.ZERO);

        ResponseEntity<SellerFinanceSummaryResponse> result1 = controller.summary(storeId, from, to);
        ResponseEntity<SellerFinanceSummaryResponse> result2 = controller.summary(storeId, anotherFrom, anotherTo);

        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
    }

    @Test
    void testSummary_NegativeAmounts() {
        when(sellerLedgerService.sumByType(eq(storeId), eq(SellerLedgerEntryType.PLATFORM_FEE), eq(from), eq(to)))
            .thenReturn(BigDecimal.valueOf(-50.00));
        when(sellerLedgerService.sumByType(eq(storeId), any(SellerLedgerEntryType.class), eq(from), eq(to)))
            .thenReturn(BigDecimal.ZERO);

        ResponseEntity<SellerFinanceSummaryResponse> result = controller.summary(storeId, from, to);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testSummary_LargeAmounts() {
        when(sellerLedgerService.sumByType(eq(storeId), any(SellerLedgerEntryType.class), eq(from), eq(to)))
            .thenReturn(BigDecimal.valueOf(9999999.99));

        ResponseEntity<SellerFinanceSummaryResponse> result = controller.summary(storeId, from, to);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testSummary_MultipleCallsSameStore() {
        when(sellerLedgerService.sumByType(eq(storeId), any(SellerLedgerEntryType.class), any(), any()))
            .thenReturn(BigDecimal.ZERO);

        controller.summary(storeId, from, to);
        controller.summary(storeId, from, to);

        verify(sellerLedgerService, times(22)).sumByType(eq(storeId), any(SellerLedgerEntryType.class), eq(from), eq(to));
    }
}
