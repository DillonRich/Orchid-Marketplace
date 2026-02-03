package com.orchid.orchid_marketplace.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class StoreResponse {
    private UUID id;
    private UUID sellerId;
    private String storeName;
    private String slug;
    private String profileImageUrl;
    private String bannerImageUrl;
    private String aboutText;
    private String returnPolicyText;
    private Integer totalSales;
    private BigDecimal averageRating;
    private Boolean isPublic;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getSellerId() { return sellerId; }
    public void setSellerId(UUID sellerId) { this.sellerId = sellerId; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getBannerImageUrl() { return bannerImageUrl; }
    public void setBannerImageUrl(String bannerImageUrl) { this.bannerImageUrl = bannerImageUrl; }

    public String getAboutText() { return aboutText; }
    public void setAboutText(String aboutText) { this.aboutText = aboutText; }

    public String getReturnPolicyText() { return returnPolicyText; }
    public void setReturnPolicyText(String returnPolicyText) { this.returnPolicyText = returnPolicyText; }

    public Integer getTotalSales() { return totalSales; }
    public void setTotalSales(Integer totalSales) { this.totalSales = totalSales; }

    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
}
