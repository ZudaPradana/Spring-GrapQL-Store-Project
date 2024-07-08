package org.zydd.bebtpn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zydd.bebtpn.entity.Customers;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Long> {
    Page<Customers> findByCustomerNameContainingIgnoreCaseAndIsActive(String customerName, Boolean isActive, Pageable pageable);
    Page<Customers> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);
    Page<Customers> findByIsActive(Boolean isActive, Pageable pageable);
}


