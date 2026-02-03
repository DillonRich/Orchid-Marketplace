package com.orchid.orchid_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orchid.orchid_marketplace.model.*;
import com.orchid.orchid_marketplace.repository.SellerLedgerEntryRepository;
import com.orchid.orchid_marketplace.repository.StoreRepository;

@ExtendWith(MockitoExtension.class)
class SellerLedgerServiceTest {
    
    @Mock
    private SellerLedgerEntryRepository sellerLedgerEntryRepository;
    
    @Mock
    private StoreRepository storeRepository;
    
    @InjectMocks
    private SellerLedgerService sellerLedgerService;
    
    private Store store;
    private Product product;
    private SellerLedgerEntry ledgerEntry;
    private UUID storeId;
    private UUID productId;
    private LocalDateTime from;
    private LocalDateTime to;
    
    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        productId = UUID.randomUUID();
        from = LocalDateTime.now().minusDays(30);
        to = LocalDateTime.now();
        
        store = new Store();
        store.setId(storeId);
        store.setStoreName("Test Store");
        
        product = new Product();
        product.setId(productId);
        product.setTitle("Test Product");
        product.setStore(store);
        
        ledgerEntry = new SellerLedgerEntry();
        ledgerEntry.setId(UUID.randomUUID());
        ledgerEntry.setStore(store);
        ledgerEntry.setType(SellerLedgerEntryType.LISTING_FEE_ACCRUED);
        ledgerEntry.setAmount(SellerLedgerService.DEFAULT_LISTING_FEE);
        ledgerEntry.setAffectsSellerBalance(true);
        ledgerEntry.setProductId(productId);
        ledgerEntry.setIsSettled(false);
    }
    
    @Test
    void testAccrueListingFeeForProduct() {
        when(sellerLedgerEntryRepository.save(any(SellerLedgerEntry.class))).thenReturn(ledgerEntry);
        
        sellerLedgerService.accrueListingFeeForProduct(product);
        
        verify(sellerLedgerEntryRepository, times(1)).save(any(SellerLedgerEntry.class));
    }
    
    @Test
    void testAccrueListingFeeForProductNullProductThrowsException() {
        assertThrows(NullPointerException.class, () -> sellerLedgerService.accrueListingFeeForProduct(null));
    }
    
    @Test
    void testAccrueListingFeeForProductNullStoreThrowsException() {
        product.setStore(null);
        assertThrows(NullPointerException.class, () -> sellerLedgerService.accrueListingFeeForProduct(product));
    }
    
    @Test
    void testAccrueListingFeeForProductUsesCorrectDefaultFee() {
        when(sellerLedgerEntryRepository.save(any(SellerLedgerEntry.class))).thenAnswer(invocation -> {
            SellerLedgerEntry saved = invocation.getArgument(0);
            assertEquals(SellerLedgerService.DEFAULT_LISTING_FEE, saved.getAmount());
            assertEquals(SellerLedgerEntryType.LISTING_FEE_ACCRUED, saved.getType());
            assertFalse(saved.getIsSettled());
            assertTrue(saved.getAffectsSellerBalance());
            return saved;
        });
        
        sellerLedgerService.accrueListingFeeForProduct(product);
        
        verify(sellerLedgerEntryRepository).save(any(SellerLedgerEntry.class));
    }
    
    @Test
    void testCountOutstandingListingFees() {
        when(sellerLedgerEntryRepository.countByStoreIdAndTypeAndIsSettledFalse(
            storeId, SellerLedgerEntryType.LISTING_FEE_ACCRUED)).thenReturn(5L);
        
        long result = sellerLedgerService.countOutstandingListingFees(storeId);
        
        assertEquals(5L, result);
        verify(sellerLedgerEntryRepository, times(1)).countByStoreIdAndTypeAndIsSettledFalse(
            storeId, SellerLedgerEntryType.LISTING_FEE_ACCRUED);
    }
    
    @Test
    void testCountOutstandingListingFeesNullStoreIdThrowsException() {
        assertThrows(NullPointerException.class, () -> sellerLedgerService.countOutstandingListingFees(null));
    }
    
    @Test
    void testGetLedgerEntries() {
        List<SellerLedgerEntry> entries = List.of(ledgerEntry);
        when(sellerLedgerEntryRepository.findByStoreIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            storeId, from, to)).thenReturn(entries);
        
        List<SellerLedgerEntry> result = sellerLedgerService.getLedgerEntries(storeId, from, to);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sellerLedgerEntryRepository, times(1)).findByStoreIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            storeId, from, to);
    }
    
    @Test
    void testGetLedgerEntriesNullStoreIdThrowsException() {
        assertThrows(NullPointerException.class, () -> sellerLedgerService.getLedgerEntries(null, from, to));
    }
    
    @Test
    void testGetLedgerEntriesNullFromThrowsException() {
        assertThrows(NullPointerException.class, () -> sellerLedgerService.getLedgerEntries(storeId, null, to));
    }
    
    @Test
    void testGetLedgerEntriesNullToThrowsException() {
        assertThrows(NullPointerException.class, () -> sellerLedgerService.getLedgerEntries(storeId, from, null));
    }
    
    @Test
    void testSumByType() {
        BigDecimal expectedSum = new BigDecimal("150.00");
        when(sellerLedgerEntryRepository.sumByTypeBetween(
                storeId, SellerLedgerEntryType.SALE_SUBTOTAL, from, to)).thenReturn(expectedSum);
        
            BigDecimal result = sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.SALE_SUBTOTAL, from, to);
        
        assertEquals(expectedSum, result);
        verify(sellerLedgerEntryRepository, times(1)).sumByTypeBetween(
                storeId, SellerLedgerEntryType.SALE_SUBTOTAL, from, to);
    }
    
    @Test
    void testSumByTypeNullStoreIdThrowsException() {
        assertThrows(NullPointerException.class, 
                () -> sellerLedgerService.sumByType(null, SellerLedgerEntryType.SALE_SUBTOTAL, from, to));
    }
    
    @Test
    void testSumByTypeNullTypeThrowsException() {
        assertThrows(NullPointerException.class, 
            () -> sellerLedgerService.sumByType(storeId, null, from, to));
    }
    
    @Test
    void testSumByTypeNullFromThrowsException() {
        assertThrows(NullPointerException.class, 
                () -> sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.SALE_SUBTOTAL, null, to));
    }
    
    @Test
    void testSumByTypeNullToThrowsException() {
        assertThrows(NullPointerException.class, 
                () -> sellerLedgerService.sumByType(storeId, SellerLedgerEntryType.SALE_SUBTOTAL, from, null));
    }
    
    @Test
    void testSettleOldestListingFees() {
        UUID orderId = UUID.randomUUID();
        List<SellerLedgerEntry> outstandingFees = List.of(ledgerEntry, ledgerEntry, ledgerEntry);
        
        when(sellerLedgerEntryRepository.findByStoreIdAndTypeAndIsSettledFalseOrderByCreatedAtAsc(
            storeId, SellerLedgerEntryType.LISTING_FEE_ACCRUED)).thenReturn(outstandingFees);
        when(sellerLedgerEntryRepository.save(any(SellerLedgerEntry.class))).thenReturn(ledgerEntry);
        
        long result = sellerLedgerService.settleOldestListingFees(storeId, 75L, orderId);
        
        assertEquals(3L, result);
        verify(sellerLedgerEntryRepository).findByStoreIdAndTypeAndIsSettledFalseOrderByCreatedAtAsc(
            storeId, SellerLedgerEntryType.LISTING_FEE_ACCRUED);
    }
    
    @Test
    void testSettleOldestListingFeesNullStoreIdThrowsException() {
        UUID orderId = UUID.randomUUID();
        assertThrows(NullPointerException.class, 
            () -> sellerLedgerService.settleOldestListingFees(null, 75L, orderId));
    }
    
    @Test
    void testSettleOldestListingFeesNullOrderIdThrowsException() {
        assertThrows(NullPointerException.class, 
            () -> sellerLedgerService.settleOldestListingFees(storeId, 75L, null));
    }
    
    @Test
    void testSettleOldestListingFeesZeroAmountReturnsZero() {
        UUID orderId = UUID.randomUUID();
        long result = sellerLedgerService.settleOldestListingFees(storeId, 0L, orderId);
        
        assertEquals(0L, result);
        verify(sellerLedgerEntryRepository, never()).findByStoreIdAndTypeAndIsSettledFalseOrderByCreatedAtAsc(
            any(), any());
    }
    
    @Test
    void testSettleOldestListingFeesNegativeAmountReturnsZero() {
        UUID orderId = UUID.randomUUID();
        long result = sellerLedgerService.settleOldestListingFees(storeId, -50L, orderId);
        
        assertEquals(0L, result);
        verify(sellerLedgerEntryRepository, never()).findByStoreIdAndTypeAndIsSettledFalseOrderByCreatedAtAsc(
            any(), any());
    }
}
