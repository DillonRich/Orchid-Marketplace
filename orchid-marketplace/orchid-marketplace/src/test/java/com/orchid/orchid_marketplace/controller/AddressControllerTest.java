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

import com.orchid.orchid_marketplace.dto.AddressRequest;
import com.orchid.orchid_marketplace.dto.AddressResponse;
import com.orchid.orchid_marketplace.model.Address;
import com.orchid.orchid_marketplace.service.AddressService;

class AddressControllerTest {

    private AddressController controller;

    @Mock
    private AddressService addressService;

    private UUID addressId;
    private Address testAddress;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AddressController();
        ReflectionTestUtils.setField(controller, "addressService", addressService);

        addressId = UUID.randomUUID();
        testAddress = new Address();
        testAddress.setId(addressId);
        testAddress.setStreetAddress("123 Main St");
        testAddress.setCity("Test City");
    }

    // ========== listAll Tests ==========

    @Test
    void testListAll_Success() {
        when(addressService.getAll()).thenReturn(List.of(testAddress));

        List<AddressResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListAll_Empty() {
        when(addressService.getAll()).thenReturn(List.of());

        List<AddressResponse> result = controller.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAll_MultipleAddresses() {
        Address address2 = new Address();
        address2.setId(UUID.randomUUID());
        address2.setStreetAddress("456 Oak Ave");

        when(addressService.getAll()).thenReturn(List.of(testAddress, address2));

        List<AddressResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ========== getById Tests ==========

    @Test
    void testGetById_Success() {
        when(addressService.getById(addressId)).thenReturn(Optional.of(testAddress));

        ResponseEntity<AddressResponse> result = controller.getById(addressId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(addressService.getById(addressId)).thenReturn(Optional.empty());

        ResponseEntity<AddressResponse> result = controller.getById(addressId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetById_VerifyServiceCalled() {
        when(addressService.getById(addressId)).thenReturn(Optional.of(testAddress));

        controller.getById(addressId);

        verify(addressService).getById(addressId);
    }

    // ========== create Tests ==========

    @Test
    void testCreate_Success() {
        AddressRequest request = new AddressRequest();

        when(addressService.create(any(Address.class))).thenReturn(testAddress);

        AddressResponse result = controller.create(request);

        assertNotNull(result);
    }

    @Test
    void testCreate_VerifyServiceCalled() {
        AddressRequest request = new AddressRequest();

        when(addressService.create(any(Address.class))).thenReturn(testAddress);

        controller.create(request);

        verify(addressService).create(any(Address.class));
    }

    // ========== update Tests ==========

    @Test
    void testUpdate_Success() {
        AddressRequest request = new AddressRequest();

        when(addressService.update(eq(addressId), any(Address.class))).thenReturn(testAddress);

        AddressResponse result = controller.update(addressId, request);

        assertNotNull(result);
    }

    @Test
    void testUpdate_VerifyServiceCalled() {
        AddressRequest request = new AddressRequest();

        when(addressService.update(eq(addressId), any(Address.class))).thenReturn(testAddress);

        controller.update(addressId, request);

        verify(addressService).update(eq(addressId), any(Address.class));
    }

    // ========== delete Tests ==========

    @Test
    void testDelete_Success() {
        doNothing().when(addressService).deleteAddress(addressId);

        ResponseEntity<Void> result = controller.delete(addressId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDelete_VerifyServiceCalled() {
        doNothing().when(addressService).deleteAddress(addressId);

        controller.delete(addressId);

        verify(addressService).deleteAddress(addressId);
    }
}
