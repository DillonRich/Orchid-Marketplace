package com.orchid.orchid_marketplace.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.model.Address;
import com.orchid.orchid_marketplace.repository.AddressRepository;

@Service
@Profile("!cosmos")
public class AddressService {
    
    @Autowired
    private AddressRepository repository;
    
    public List<Address> getAll() {
        return repository.findAll();
    }
    
    public Optional<Address> getById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return repository.findById(id);
    }
    
    public List<Address> getByUserId(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return repository.findByUserId(userId);
    }
    
    public Optional<Address> getDefaultAddressByUser(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return repository.findByUserIdAndIsDefaultTrue(userId);
    }
    
    public Address create(Address address) {
        Objects.requireNonNull(address, "address must not be null");
        @SuppressWarnings("null")
        Address saved = repository.save(address);
        return saved;
    }
    
    public Address update(UUID id, Address addressDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(addressDetails, "addressDetails must not be null");
        return repository.findById(id)
            .map(existing -> {
                if (addressDetails.getStreetAddress() != null) {
                    existing.setStreetAddress(addressDetails.getStreetAddress());
                }
                
                if (addressDetails.getCity() != null) {
                    existing.setCity(addressDetails.getCity());
                }
                
                if (addressDetails.getState() != null) {
                    existing.setState(addressDetails.getState());
                }
                
                if (addressDetails.getCountry() != null) {
                    existing.setCountry(addressDetails.getCountry());
                }
                
                if (addressDetails.getPostalCode() != null) {
                    existing.setPostalCode(addressDetails.getPostalCode());
                }
                
                if (addressDetails.getIsDefault() != null) {
                    existing.setIsDefault(addressDetails.getIsDefault());
                }

                @SuppressWarnings("null")
                Address saved = repository.save(existing);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Address not found"));
    }
    
    public void deleteAddress(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        repository.findById(id)
            .ifPresentOrElse(
                address -> {
                    address.softDelete();
                    repository.save(address);
                },
                () -> { throw new RuntimeException("Address not found with ID: " + id); }
            );
    }
}