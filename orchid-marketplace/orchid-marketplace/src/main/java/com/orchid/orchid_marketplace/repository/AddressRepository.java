package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByUserId(UUID userId);
    List<Address> findByCity(String city);
    List<Address> findByState(String state);
    List<Address> findByCountry(String country);
    List<Address> findByPostalCode(String postalCode);  // Changed from zipCode to postalCode
    Optional<Address> findByUserIdAndIsDefaultTrue(UUID userId);
}