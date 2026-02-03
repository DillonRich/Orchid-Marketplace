package com.orchid.orchid_marketplace.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.model.Notification;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.NotificationRepository;
import com.orchid.orchid_marketplace.repository.UserRepository;

@Service
@Profile("!cosmos")
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(
        NotificationRepository notificationRepository,
        UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @SuppressWarnings("null")
    public Notification createNotification(
        UUID recipientId,
        Notification.NotificationType type,
        Notification.NotificationChannel channel,
        String title,
        String message,
        UUID relatedOrderId,
        UUID relatedProductId
    ) {
        User recipient = userRepository.findById(recipientId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setType(type);
        notification.setChannel(channel);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedOrderId(relatedOrderId);
        notification.setRelatedProductId(relatedProductId);
        notification.setSentAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @Transactional
    public void sendOrderConfirmationNotification(UUID orderId, UUID buyerId) {
        createNotification(
            buyerId,
            Notification.NotificationType.ORDER_CONFIRMED,
            Notification.NotificationChannel.EMAIL,
            "Order Confirmed",
            "Your order #" + orderId + " has been confirmed.",
            orderId,
            null
        );
    }

    @Transactional
    public void sendShippingNotification(UUID orderId, UUID buyerId, String trackingNumber) {
        createNotification(
            buyerId,
            Notification.NotificationType.ORDER_SHIPPED,
            Notification.NotificationChannel.EMAIL,
            "Order Shipped",
            "Your order #" + orderId + " has been shipped. Tracking: " + trackingNumber,
            orderId,
            null
        );
    }

    @Transactional
    public void sendDeliveryNotification(UUID orderId, UUID buyerId) {
        createNotification(
            buyerId,
            Notification.NotificationType.ORDER_DELIVERED,
            Notification.NotificationChannel.EMAIL,
            "Delivery Complete",
            "Your order #" + orderId + " has been delivered.",
            orderId,
            null
        );
    }

    @Transactional
    public void sendPaymentConfirmationNotification(UUID orderId, UUID buyerId) {
        createNotification(
            buyerId,
            Notification.NotificationType.PAYMENT_RECEIVED,
            Notification.NotificationChannel.EMAIL,
            "Payment Received",
            "Payment for order #" + orderId + " has been received.",
            orderId,
            null
        );
    }

    @Transactional
    public void sendPaymentFailedNotification(UUID orderId, UUID buyerId) {
        createNotification(
            buyerId,
            Notification.NotificationType.PAYMENT_FAILED,
            Notification.NotificationChannel.EMAIL,
            "Payment Failed",
            "Payment for order #" + orderId + " failed. Please try again.",
            orderId,
            null
        );
    }

    @Transactional
    public void sendReviewNotification(UUID productId, UUID sellerId, String reviewerName) {
        createNotification(
            sellerId,
            Notification.NotificationType.REVIEW_POSTED,
            Notification.NotificationChannel.IN_APP,
            "New Review",
            "A new review from " + reviewerName + " has been posted on your product.",
            null,
            productId
        );
    }

    @Transactional(readOnly = true)
    public Page<Notification> getUserNotifications(UUID userId, Pageable pageable) {
        return notificationRepository.findByRecipientId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(UUID userId) {
        return notificationRepository.findByRecipientIdAndIsReadFalse(userId);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(UUID userId) {
        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);
    }

    @Transactional
    @SuppressWarnings("null")
    public Notification markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @Transactional
    @SuppressWarnings("null")
    public void deleteNotification(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
