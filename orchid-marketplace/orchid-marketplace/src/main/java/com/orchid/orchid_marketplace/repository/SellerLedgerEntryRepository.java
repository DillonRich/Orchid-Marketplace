package com.orchid.orchid_marketplace.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.SellerLedgerEntry;
import com.orchid.orchid_marketplace.model.SellerLedgerEntryType;

@Repository
public interface SellerLedgerEntryRepository extends JpaRepository<SellerLedgerEntry, UUID> {

    List<SellerLedgerEntry> findByStoreIdOrderByCreatedAtDesc(UUID storeId);

    List<SellerLedgerEntry> findByStoreIdAndCreatedAtBetweenOrderByCreatedAtDesc(UUID storeId, LocalDateTime from, LocalDateTime to);

    List<SellerLedgerEntry> findByStoreIdAndTypeAndIsSettledFalseOrderByCreatedAtAsc(UUID storeId, SellerLedgerEntryType type);

    long countByStoreIdAndTypeAndIsSettledFalse(UUID storeId, SellerLedgerEntryType type);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM SellerLedgerEntry e WHERE e.store.id = :storeId AND e.affectsSellerBalance = true AND e.createdAt BETWEEN :from AND :to")
    BigDecimal sumAffectingSellerBalanceBetween(@Param("storeId") UUID storeId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM SellerLedgerEntry e WHERE e.store.id = :storeId AND e.type = :type AND e.createdAt BETWEEN :from AND :to")
    BigDecimal sumByTypeBetween(@Param("storeId") UUID storeId, @Param("type") SellerLedgerEntryType type, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
