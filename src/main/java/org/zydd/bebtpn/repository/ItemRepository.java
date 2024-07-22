package org.zydd.bebtpn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zydd.bebtpn.entity.Customers;
import org.zydd.bebtpn.entity.Items;

@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {

    // Search by item name only
    Page<Items> findByItemNameContainingIgnoreCase(String itemName, Pageable pageable);

    // Search by availability only
    Page<Items> findByIsAvailable(Boolean isAvailable, Pageable pageable);

    // Search by item name and availability
    Page<Items> findByItemNameContainingIgnoreCaseAndIsAvailable(String itemName, Boolean isAvailable, Pageable pageable);
}