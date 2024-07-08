package org.zydd.bebtpn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zydd.bebtpn.entity.Items;

@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {
}
