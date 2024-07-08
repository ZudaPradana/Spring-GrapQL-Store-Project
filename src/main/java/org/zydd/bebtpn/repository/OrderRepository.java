package org.zydd.bebtpn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zydd.bebtpn.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
}
