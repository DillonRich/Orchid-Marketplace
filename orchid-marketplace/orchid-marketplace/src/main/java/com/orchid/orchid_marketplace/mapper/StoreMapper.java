package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.StoreRequest;
import com.orchid.orchid_marketplace.dto.StoreResponse;
import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.model.User;

public final class StoreMapper {
    private StoreMapper() {}

    public static Store toEntity(StoreRequest req) {
        if (req == null) return null;
        Store s = new Store();
        User u = new User(); u.setId(req.getSellerId()); s.setSeller(u);
        s.setStoreName(req.getStoreName());
        s.setSlug(req.getSlug());
        s.setProfileImageUrl(req.getProfileImageUrl());
        s.setBannerImageUrl(req.getBannerImageUrl());
        s.setAboutText(req.getAboutText());
        s.setReturnPolicyText(req.getReturnPolicyText());
        s.setIsPublic(req.getIsPublic());
        return s;
    }

    public static StoreResponse toResponse(Store s) {
        if (s == null) return null;
        StoreResponse r = new StoreResponse();
        r.setId(s.getId());
        r.setSellerId(s.getSeller() != null ? s.getSeller().getId() : null);
        r.setStoreName(s.getStoreName());
        r.setSlug(s.getSlug());
        r.setProfileImageUrl(s.getProfileImageUrl());
        r.setBannerImageUrl(s.getBannerImageUrl());
        r.setAboutText(s.getAboutText());
        r.setReturnPolicyText(s.getReturnPolicyText());
        r.setTotalSales(s.getTotalSales());
        r.setAverageRating(s.getAverageRating());
        r.setIsPublic(s.getIsPublic());
        return r;
    }
}
