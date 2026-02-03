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

import com.orchid.orchid_marketplace.dto.CacheConfigurationRequest;
import com.orchid.orchid_marketplace.dto.CacheConfigurationResponse;
import com.orchid.orchid_marketplace.model.CacheConfiguration;
import com.orchid.orchid_marketplace.service.CacheConfigurationService;

class CacheConfigurationControllerTest {

    private CacheConfigurationController controller;

    @Mock
    private CacheConfigurationService cacheService;

    private UUID configId;
    private CacheConfiguration testConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new CacheConfigurationController();
        ReflectionTestUtils.setField(controller, "cacheService", cacheService);

        configId = UUID.randomUUID();
        testConfig = new CacheConfiguration();
        testConfig.setId(configId);
        testConfig.setCacheKeyPattern("product:*");
    }

    // ========== listAll Tests ==========

    @Test
    void testListAll_Success() {
        when(cacheService.getAllCacheConfigurations()).thenReturn(List.of(testConfig));

        List<CacheConfigurationResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListAll_Empty() {
        when(cacheService.getAllCacheConfigurations()).thenReturn(List.of());

        List<CacheConfigurationResponse> result = controller.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAll_MultipleConfigurations() {
        CacheConfiguration config2 = new CacheConfiguration();
        config2.setId(UUID.randomUUID());
        config2.setCacheKeyPattern("category:*");

        when(cacheService.getAllCacheConfigurations()).thenReturn(List.of(testConfig, config2));

        List<CacheConfigurationResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ========== getById Tests ==========

    @Test
    void testGetById_Success() {
        when(cacheService.getCacheConfigurationById(configId)).thenReturn(Optional.of(testConfig));

        ResponseEntity<CacheConfigurationResponse> result = controller.getById(configId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(cacheService.getCacheConfigurationById(configId)).thenReturn(Optional.empty());

        ResponseEntity<CacheConfigurationResponse> result = controller.getById(configId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetById_VerifyServiceCalled() {
        when(cacheService.getCacheConfigurationById(configId)).thenReturn(Optional.of(testConfig));

        controller.getById(configId);

        verify(cacheService).getCacheConfigurationById(configId);
    }

    // ========== create Tests ==========

    @Test
    void testCreate_Success() {
        CacheConfigurationRequest request = new CacheConfigurationRequest();

        when(cacheService.createCacheConfiguration(any(CacheConfiguration.class))).thenReturn(testConfig);

        CacheConfigurationResponse result = controller.create(request);

        assertNotNull(result);
    }

    @Test
    void testCreate_VerifyServiceCalled() {
        CacheConfigurationRequest request = new CacheConfigurationRequest();

        when(cacheService.createCacheConfiguration(any(CacheConfiguration.class))).thenReturn(testConfig);

        controller.create(request);

        verify(cacheService).createCacheConfiguration(any(CacheConfiguration.class));
    }

    // ========== update Tests ==========

    @Test
    void testUpdate_Success() {
        CacheConfigurationRequest request = new CacheConfigurationRequest();

        when(cacheService.updateCacheConfiguration(eq(configId), any(CacheConfiguration.class)))
            .thenReturn(testConfig);

        CacheConfigurationResponse result = controller.update(configId, request);

        assertNotNull(result);
    }

    @Test
    void testUpdate_VerifyServiceCalled() {
        CacheConfigurationRequest request = new CacheConfigurationRequest();

        when(cacheService.updateCacheConfiguration(eq(configId), any(CacheConfiguration.class)))
            .thenReturn(testConfig);

        controller.update(configId, request);

        verify(cacheService).updateCacheConfiguration(eq(configId), any(CacheConfiguration.class));
    }

    // ========== delete Tests ==========

    @Test
    void testDelete_Success() {
        doNothing().when(cacheService).deleteCacheConfiguration(configId);

        ResponseEntity<Void> result = controller.delete(configId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDelete_VerifyServiceCalled() {
        doNothing().when(cacheService).deleteCacheConfiguration(configId);

        controller.delete(configId);

        verify(cacheService).deleteCacheConfiguration(configId);
    }
}
