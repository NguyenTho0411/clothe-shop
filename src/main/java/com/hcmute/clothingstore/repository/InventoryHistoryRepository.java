package com.hcmute.clothingstore.repository;

import com.hcmute.clothingstore.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<Inventory,Long> {
}
