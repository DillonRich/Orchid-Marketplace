package com.orchid.orchid_marketplace.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.orchid.orchid_marketplace.dto.ProductSearchDTO;
import com.orchid.orchid_marketplace.service.ProductSearchService;

import java.util.List;

@WebMvcTest(controllers = ProductSearchController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("azure")
class ProductSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductSearchService searchService;

    @Test
    void search_defaultsToRelevance_unsortedPageable() throws Exception {
        when(searchService.searchProducts(any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(Page.empty());

        mockMvc.perform(get("/api/products/search")
                .param("q", "laptop")
                .param("page", "1")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(searchService, times(1)).searchProducts(eq("laptop"), any(), any(), any(), any(), any(), eq("relevance"), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        // relevance => unsorted
        org.junit.jupiter.api.Assertions.assertTrue(pageable.getSort().isUnsorted());
        org.junit.jupiter.api.Assertions.assertEquals(1, pageable.getPageNumber());
        org.junit.jupiter.api.Assertions.assertEquals(5, pageable.getPageSize());
    }

    @Test
    void search_priceDesc_sortsDescending() throws Exception {
        Page<ProductSearchDTO> page = new PageImpl<>(List.of());
        when(searchService.searchProducts(any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(page);

        mockMvc.perform(get("/api/products/search")
                .param("q", "phone")
                .param("sortBy", "price_desc"))
            .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(searchService).searchProducts(eq("phone"), any(), any(), any(), any(), any(), eq("price_desc"), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        org.junit.jupiter.api.Assertions.assertTrue(pageable.getSort().getOrderFor("price").isDescending());
    }
}
