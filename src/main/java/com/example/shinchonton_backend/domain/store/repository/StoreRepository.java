package com.example.shinchonton_backend.domain.store.repository;

import com.example.shinchonton_backend.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByMerchantIdOrderByCreatedAtDesc(Long merchantId);

    CharSequence findAllByMerchantId(Long id);
}
