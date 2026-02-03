package com.orchid.orchid_marketplace.service.azure;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.blob.models.PublicAccessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing blob storage in Azure Storage Accounts.
 * Handles upload, download, deletion, and URL generation for product and store images.
 */
@Service
@ConditionalOnProperty(prefix = "azure.storage", name = "enabled", havingValue = "true")
public class AzureBlobStorageService {

    private static final Logger logger = LoggerFactory.getLogger(AzureBlobStorageService.class);
    
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    
    private final BlobServiceClient blobServiceClient;
    
    @Value("${azure.storage.container.product-images:product-images}")
    private String productImagesContainer;
    
    @Value("${azure.storage.container.store-images:store-images}")
    private String storeImagesContainer;

    public AzureBlobStorageService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    /**
     * Initialize containers if they don't exist.
     * Should be called on application startup.
     */
    public void initializeContainers() {
        ensureContainerExists(productImagesContainer);
        ensureContainerExists(storeImagesContainer);
        logger.info("Azure Blob Storage containers initialized: {}, {}", 
            productImagesContainer, storeImagesContainer);
    }

    /**
     * Upload a product image to Azure Blob Storage.
     * 
     * @param file the multipart file to upload
     * @param productId the product UUID
     * @return the public URL of the uploaded blob
     * @throws IllegalArgumentException if file validation fails
     * @throws IOException if upload fails
     */
    public String uploadProductImage(MultipartFile file, UUID productId) throws IOException {
        validateImageFile(file);
        String blobName = generateBlobName(productId, file.getOriginalFilename());
        return uploadBlob(productImagesContainer, blobName, file);
    }

    /**
     * Upload a store image (logo/banner) to Azure Blob Storage.
     * 
     * @param file the multipart file to upload
     * @param storeId the store UUID
     * @param imageType "logo" or "banner"
     * @return the public URL of the uploaded blob
     * @throws IllegalArgumentException if file validation fails
     * @throws IOException if upload fails
     */
    public String uploadStoreImage(MultipartFile file, UUID storeId, String imageType) throws IOException {
        validateImageFile(file);
        String blobName = String.format("%s/%s/%s", storeId, imageType, 
            UUID.randomUUID() + getFileExtension(file.getOriginalFilename()));
        return uploadBlob(storeImagesContainer, blobName, file);
    }

    /**
     * Delete a blob by its full URL.
     * 
     * @param blobUrl the full URL of the blob to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteBlob(String blobUrl) {
        try {
            String[] parts = extractContainerAndBlobName(blobUrl);
            String containerName = parts[0];
            String blobName = parts[1];
            
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            
            blobClient.delete();
            logger.info("Deleted blob: {}", blobUrl);
            return true;
        } catch (BlobStorageException e) {
            logger.error("Failed to delete blob: {}", blobUrl, e);
            return false;
        }
    }

    /**
     * Get a blob as an InputStream (for download or processing).
     * 
     * @param blobUrl the full URL of the blob
     * @return InputStream of the blob content
     * @throws BlobStorageException if blob doesn't exist
     */
    public InputStream downloadBlob(String blobUrl) {
        String[] parts = extractContainerAndBlobName(blobUrl);
        String containerName = parts[0];
        String blobName = parts[1];
        
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        
        return blobClient.openInputStream();
    }

    // ============ Private Helper Methods ============

    private void ensureContainerExists(String containerName) {
        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            if (!containerClient.exists()) {
                containerClient.create();
                containerClient.setAccessPolicy(PublicAccessType.BLOB, null);
                logger.info("Created blob container: {}", containerName);
            }
        } catch (BlobStorageException e) {
            logger.error("Failed to create/check container: {}", containerName, e);
            throw new RuntimeException("Failed to initialize Azure Storage container: " + containerName, e);
        }
    }

    private String uploadBlob(String containerName, String blobName, MultipartFile file) throws IOException {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        try (InputStream inputStream = file.getInputStream()) {
            BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType(file.getContentType())
                .setCacheControl("public, max-age=31536000"); // 1 year cache

            blobClient.upload(BinaryData.fromStream(inputStream, file.getSize()), true);
            blobClient.setHttpHeaders(headers);

            String blobUrl = blobClient.getBlobUrl();
            logger.info("Uploaded blob: {} (size: {} bytes)", blobUrl, file.getSize());
            return blobUrl;
        } catch (BlobStorageException e) {
            logger.error("Failed to upload blob: {}/{}", containerName, blobName, e);
            throw new IOException("Failed to upload image to Azure Storage", e);
        }
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                String.format("File size exceeds maximum allowed size of %d MB", MAX_FILE_SIZE / (1024 * 1024))
            );
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException(
                "Invalid file type. Allowed types: " + String.join(", ", ALLOWED_IMAGE_TYPES)
            );
        }
    }

    private String generateBlobName(UUID productId, String originalFilename) {
        String extension = getFileExtension(originalFilename);
        return String.format("%s/%s%s", productId, UUID.randomUUID(), extension);
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg"; // default
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private String[] extractContainerAndBlobName(String blobUrl) {
        // Example URL: https://accountname.blob.core.windows.net/container-name/blob-name
        try {
            String[] urlParts = blobUrl.split("/", 5);
            String containerName = urlParts[3];
            String blobName = urlParts[4];
            return new String[]{containerName, blobName};
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid blob URL format: " + blobUrl, e);
        }
    }
}
