package com.example.shinchonton_backend.domain.recommend.repository;

import com.example.shinchonton_backend.domain.recommend.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    Optional<Recommendation> findByPartyRequest_IdAndStore_IdAndTableBudget(
            Long partyRequestId,
            Long storeId,
            long tableBudget
    );
}
