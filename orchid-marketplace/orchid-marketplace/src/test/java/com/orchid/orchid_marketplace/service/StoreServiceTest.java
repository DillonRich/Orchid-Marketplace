package com.orchid.orchid_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.repository.StoreRepository;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    
    @Mock
    private StoreRepository storeRepository;
    
    @InjectMocks
    private StoreService storeService;
    
    private Store store;
    private User seller;
    private UUID storeId;
    private UUID sellerId;
    
    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        sellerId = UUID.randomUUID();
        
        seller = new User();
        seller.setId(sellerId);
        seller.setEmail("seller@test.com");
        seller.setFullName("Test Seller");
        seller.setRole(Role.SELLER);
        
        store = new Store();
        store.setId(storeId);
        store.setStoreName("Test Store");
        store.setSlug("test-store");
        store.setSeller(seller);
        store.setIsActive(true);
        store.setIsPublic(true);
        store.setAverageRating(new BigDecimal("4.5"));
        store.setTotalSales(100);
    }
    
    @Test
    void testGetAllStores() {
        List<Store> stores = List.of(store);
        when(storeRepository.findAll()).thenReturn(stores);
        
        List<Store> result = storeService.getAllStores();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(storeRepository, times(1)).findAll();
    }
    
    @Test
    void testGetStoreById() {
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        
        Optional<Store> result = storeService.getStoreById(storeId);
        
        assertTrue(result.isPresent());
        assertEquals(storeId, result.get().getId());
        verify(storeRepository, times(1)).findById(storeId);
    }
    
    @Test
    void testGetStoreByIdNullThrowsException() {
        assertThrows(NullPointerException.class, () -> storeService.getStoreById(null));
    }
    
    @Test
    void testGetStoreBySlug() {
        when(storeRepository.findBySlug("test-store")).thenReturn(Optional.of(store));
        
        Optional<Store> result = storeService.getStoreBySlug("test-store");
        
        assertTrue(result.isPresent());
        assertEquals("test-store", result.get().getSlug());
        verify(storeRepository, times(1)).findBySlug("test-store");
    }
    
    @Test
    void testGetStoreByName() {
        when(storeRepository.findByStoreName("Test Store")).thenReturn(Optional.of(store));
        
        Optional<Store> result = storeService.getStoreByName("Test Store");
        
        assertTrue(result.isPresent());
        assertEquals("Test Store", result.get().getStoreName());
        verify(storeRepository, times(1)).findByStoreName("Test Store");
    }
    
    @Test
    void testGetStoreBySellerId() {
        when(storeRepository.findBySellerId(sellerId)).thenReturn(Optional.of(store));
        
        Optional<Store> result = storeService.getStoreBySellerId(sellerId);
        
        assertTrue(result.isPresent());
        assertEquals(sellerId, result.get().getSeller().getId());
        verify(storeRepository, times(1)).findBySellerId(sellerId);
    }
    
    @Test
    void testGetStoreBySellerIdNullThrowsException() {
        assertThrows(NullPointerException.class, () -> storeService.getStoreBySellerId(null));
    }
    
    @Test
    void testGetActiveStores() {
        List<Store> stores = List.of(store);
        when(storeRepository.findByIsActiveTrue()).thenReturn(stores);
        
        List<Store> result = storeService.getActiveStores();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsActive());
        verify(storeRepository, times(1)).findByIsActiveTrue();
    }
    
    @Test
    void testGetPublicStores() {
        List<Store> stores = List.of(store);
        when(storeRepository.findByIsPublicTrueAndIsActiveTrue()).thenReturn(stores);
        
        List<Store> result = storeService.getPublicStores();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsPublic());
        assertTrue(result.get(0).getIsActive());
        verify(storeRepository, times(1)).findByIsPublicTrueAndIsActiveTrue();
    }
    
    @Test
    void testSearchStores() {
        String keyword = "test";
        List<Store> stores = List.of(store);
        when(storeRepository.searchStores(keyword)).thenReturn(stores);
        
        List<Store> result = storeService.searchStores(keyword);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(storeRepository, times(1)).searchStores(keyword);
    }
    
    @Test
    void testCreateStore() {
        when(storeRepository.existsBySlug(anyString())).thenReturn(false);
        when(storeRepository.existsByStoreName(anyString())).thenReturn(false);
        when(storeRepository.save(any(Store.class))).thenReturn(store);
        
        Store result = storeService.createStore(store);
        
        assertNotNull(result);
        verify(storeRepository, times(1)).existsBySlug(anyString());
        verify(storeRepository, times(1)).existsByStoreName(anyString());
        verify(storeRepository, times(1)).save(any(Store.class));
    }
    
    @Test
    void testCreateStoreNullThrowsException() {
        assertThrows(NullPointerException.class, () -> storeService.createStore(null));
    }
    
    @Test
    void testCreateStoreDuplicateSlugThrowsException() {
        when(storeRepository.existsBySlug("test-store")).thenReturn(true);
        
        assertThrows(RuntimeException.class, () -> storeService.createStore(store));
    }
    
    @Test
    void testCreateStoreDuplicateNameThrowsException() {
        when(storeRepository.existsBySlug(anyString())).thenReturn(false);
        when(storeRepository.existsByStoreName("Test Store")).thenReturn(true);
        
        assertThrows(RuntimeException.class, () -> storeService.createStore(store));
    }
}
