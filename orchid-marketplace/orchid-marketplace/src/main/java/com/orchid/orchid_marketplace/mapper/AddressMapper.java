package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.AddressRequest;
import com.orchid.orchid_marketplace.dto.AddressResponse;
import com.orchid.orchid_marketplace.model.Address;
import com.orchid.orchid_marketplace.model.User;

public final class AddressMapper {
    private AddressMapper() {}

    public static Address toEntity(AddressRequest req) {
        if (req == null) return null;
        Address a = new Address();
        User u = new User(); u.setId(req.getUserId()); a.setUser(u);
        a.setStreetAddress(req.getStreetAddress());
        a.setCity(req.getCity());
        a.setState(req.getState());
        a.setCountry(req.getCountry());
        a.setPostalCode(req.getPostalCode());
        a.setRecipientName(req.getRecipientName());
        a.setPhoneNumber(req.getPhoneNumber());
        try {
            if (req.getAddressType() != null) {
                a.setAddressType(Address.AddressType.valueOf(req.getAddressType()));
            }
        } catch (IllegalArgumentException e) {
            // ignore invalid type; validation should prevent this
        }
        a.setIsDefault(req.getIsDefault());
        return a;
    }

    public static AddressResponse toResponse(Address a) {
        if (a == null) return null;
        AddressResponse r = new AddressResponse();
        r.setId(a.getId());
        r.setUserId(a.getUser() != null ? a.getUser().getId() : null);
        r.setStreetAddress(a.getStreetAddress());
        r.setCity(a.getCity());
        r.setState(a.getState());
        r.setCountry(a.getCountry());
        r.setPostalCode(a.getPostalCode());
        r.setRecipientName(a.getRecipientName());
        r.setPhoneNumber(a.getPhoneNumber());
        r.setAddressType(a.getAddressType() != null ? a.getAddressType().name() : null);
        r.setIsDefault(a.getIsDefault());
        return r;
    }
}
