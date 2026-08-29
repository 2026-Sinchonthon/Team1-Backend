package com.example.shinchonton_backend.domain.store.repository;

import com.example.shinchonton_backend.domain.store.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findAllByStoreId(Long storeId);
    List<MenuItem> findAllByStoreIdAndAvailableTrueOrderByIdAsc(Long storeId);
}
