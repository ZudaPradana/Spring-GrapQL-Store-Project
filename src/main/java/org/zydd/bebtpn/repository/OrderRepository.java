package org.zydd.bebtpn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zydd.bebtpn.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o " +
            "JOIN o.customers c " +
            "JOIN o.items i " +
            "WHERE c.customerName LIKE %:customerName% " +
            "AND i.itemName LIKE %:itemName%")
    Page<Orders> findByCustomerNameAndItemName(@Param("customerName") String customerName,
                                               @Param("itemName") String itemName,
                                               Pageable pageable);

    @Query("SELECT o FROM Orders o " +
            "JOIN o.customers c " +
            "WHERE c.customerName LIKE %:customerName% ")
    Page<Orders> findByCustomerName(String customerName, Pageable pageable);

    @Query("SELECT o FROM Orders o " +
            "JOIN o.items i " +
            "WHERE i.itemName LIKE %:itemName% ")
    Page<Orders> findByItemName(String itemName, Pageable pageable);
}
