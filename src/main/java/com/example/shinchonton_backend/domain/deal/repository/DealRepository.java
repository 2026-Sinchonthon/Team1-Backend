package com.example.shinchonton_backend.domain.deal.repository;

import com.example.shinchonton_backend.domain.deal.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long> {

    Optional<Deal> findByPartyRequest_Id(Long partyRequestId);

    boolean existsByPartyRequest_Id(Long partyRequestId);
}
