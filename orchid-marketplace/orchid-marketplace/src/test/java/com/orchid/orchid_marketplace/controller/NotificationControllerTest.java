package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.orchid.orchid_marketplace.model.Notification;
import com.orchid.orchid_marketplace.service.NotificationService;

class NotificationControllerTest {

    private NotificationController controller;

    @Mock
    private NotificationService notificationService;

    private UUID userId;
    private UUID notificationId;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new NotificationController(notificationService);

        userId = UUID.randomUUID();
        notificationId = UUID.randomUUID();
        testNotification = new Notification();
        testNotification.setId(notificationId);
        testNotification.setMessage("Test notification");
    }

    // ========== getNotifications Tests ==========

    @Test
    void testGetNotifications_Success() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Notification> page = new PageImpl<>(List.of(testNotification), pageable, 1);

        when(notificationService.getUserNotifications(userId, pageable)).thenReturn(page);

        ResponseEntity<Page<Notification>> result = controller.getNotifications(userId, 0, 20);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().getTotalElements());
    }

    @Test
    void testGetNotifications_Empty() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Notification> page = new PageImpl<>(List.of(), pageable, 0);

        when(notificationService.getUserNotifications(userId, pageable)).thenReturn(page);

        ResponseEntity<Page<Notification>> result = controller.getNotifications(userId, 0, 20);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(0, result.getBody().getTotalElements());
    }

    @Test
    void testGetNotifications_WithPagination() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Notification> page = new PageImpl<>(List.of(), pageable, 15);

        when(notificationService.getUserNotifications(userId, pageable)).thenReturn(page);

        ResponseEntity<Page<Notification>> result = controller.getNotifications(userId, 1, 10);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(notificationService).getUserNotifications(eq(userId), eq(pageable));
    }

    // ========== getUnreadNotifications Tests ==========

    @Test
    void testGetUnreadNotifications_Success() {
        when(notificationService.getUnreadNotifications(userId)).thenReturn(List.of(testNotification));

        ResponseEntity<List<Notification>> result = controller.getUnreadNotifications(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testGetUnreadNotifications_Empty() {
        when(notificationService.getUnreadNotifications(userId)).thenReturn(List.of());

        ResponseEntity<List<Notification>> result = controller.getUnreadNotifications(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void testGetUnreadNotifications_VerifyServiceCalled() {
        when(notificationService.getUnreadNotifications(userId)).thenReturn(List.of(testNotification));

        controller.getUnreadNotifications(userId);

        verify(notificationService).getUnreadNotifications(userId);
    }

    // ========== getUnreadCount Tests ==========

    @Test
    void testGetUnreadCount_Success() {
        when(notificationService.getUnreadCount(userId)).thenReturn(5L);

        ResponseEntity<Long> result = controller.getUnreadCount(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(5L, result.getBody());
    }

    @Test
    void testGetUnreadCount_Zero() {
        when(notificationService.getUnreadCount(userId)).thenReturn(0L);

        ResponseEntity<Long> result = controller.getUnreadCount(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(0L, result.getBody());
    }

    @Test
    void testGetUnreadCount_VerifyServiceCalled() {
        when(notificationService.getUnreadCount(userId)).thenReturn(3L);

        controller.getUnreadCount(userId);

        verify(notificationService).getUnreadCount(userId);
    }

    // ========== markAsRead Tests ==========

    @Test
    void testMarkAsRead_Success() {
        when(notificationService.markAsRead(notificationId)).thenReturn(testNotification);

        ResponseEntity<Notification> result = controller.markAsRead(notificationId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testMarkAsRead_VerifyServiceCalled() {
        when(notificationService.markAsRead(notificationId)).thenReturn(testNotification);

        controller.markAsRead(notificationId);

        verify(notificationService).markAsRead(notificationId);
    }

    // ========== deleteNotification Tests ==========

    @Test
    void testDeleteNotification_Success() {
        doNothing().when(notificationService).deleteNotification(notificationId);

        ResponseEntity<Void> result = controller.deleteNotification(notificationId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDeleteNotification_VerifyServiceCalled() {
        doNothing().when(notificationService).deleteNotification(notificationId);

        controller.deleteNotification(notificationId);

        verify(notificationService).deleteNotification(notificationId);
    }
}
