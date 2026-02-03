package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.orchid.orchid_marketplace.dto.StoreRequest;
import com.orchid.orchid_marketplace.dto.StoreResponse;
import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.service.StoreService;

class StoreControllerTest {

    private StoreController controller;

    @Mock
    private StoreService storeService;

    private UUID storeId;
    private Store testStore;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new StoreController();
        ReflectionTestUtils.setField(controller, "storeService", storeService);

        storeId = UUID.randomUUID();
        testStore = new Store();
        testStore.setId(storeId);
        testStore.setStoreName("Test Store");
    }

    // ========== listAll Tests ==========

    @Test
    void testListAll_Success() {
        when(storeService.getAllStores()).thenReturn(List.of(testStore));

        List<StoreResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListAll_Empty() {
        when(storeService.getAllStores()).thenReturn(List.of());

        List<StoreResponse> result = controller.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAll_MultipleStores() {
        Store store2 = new Store();
        store2.setId(UUID.randomUUID());
        store2.setStoreName("Store 2");

        when(storeService.getAllStores()).thenReturn(List.of(testStore, store2));

        List<StoreResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ========== getById Tests ==========

    @Test
    void testGetById_Success() {
        when(storeService.getStoreById(storeId)).thenReturn(Optional.of(testStore));

        ResponseEntity<StoreResponse> result = controller.getById(storeId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(storeService.getStoreById(storeId)).thenReturn(Optional.empty());

        ResponseEntity<StoreResponse> result = controller.getById(storeId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetById_VerifyServiceCalled() {
        when(storeService.getStoreById(storeId)).thenReturn(Optional.of(testStore));

        controller.getById(storeId);

        verify(storeService).getStoreById(storeId);
    }

    // ========== create Tests ==========

    @Test
    void testCreate_Success() {
        StoreRequest request = new StoreRequest();

        when(storeService.createStore(any(Store.class))).thenReturn(testStore);

        StoreResponse result = controller.create(request);

        assertNotNull(result);
    }

    @Test
    void testCreate_VerifyServiceCalled() {
        StoreRequest request = new StoreRequest();

        when(storeService.createStore(any(Store.class))).thenReturn(testStore);

        controller.create(request);

        verify(storeService).createStore(any(Store.class));
    }

    // ========== update Tests ==========

    @Test
    void testUpdate_Success() {
        StoreRequest request = new StoreRequest();

        when(storeService.updateStore(eq(storeId), any(Store.class))).thenReturn(testStore);

        StoreResponse result = controller.update(storeId, request);

        assertNotNull(result);
    }

    @Test
    void testUpdate_VerifyServiceCalled() {
        StoreRequest request = new StoreRequest();

        when(storeService.updateStore(eq(storeId), any(Store.class))).thenReturn(testStore);

        controller.update(storeId, request);

        verify(storeService).updateStore(eq(storeId), any(Store.class));
    }

    // ========== delete Tests ==========

    @Test
    void testDelete_Success() {
        doNothing().when(storeService).deleteStore(storeId);

        ResponseEntity<Void> result = controller.delete(storeId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDelete_VerifyServiceCalled() {
        doNothing().when(storeService).deleteStore(storeId);

        controller.delete(storeId);

        verify(storeService).deleteStore(storeId);
    }
}
