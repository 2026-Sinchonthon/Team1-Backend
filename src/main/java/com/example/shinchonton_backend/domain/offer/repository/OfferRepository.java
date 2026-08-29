package com.example.shinchonton_backend.domain.offer.repository;

import com.example.shinchonton_backend.domain.offer.entity.Offer;
import com.example.shinchonton_backend.domain.offer.entity.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    boolean existsByPartyRequest_IdAndStore_Id(Long partyRequestId, Long storeId);

    List<Offer> findAllByPartyRequest_IdOrderByCreatedAtDesc(Long partyRequestId);

    List<Offer> findAllByPartyRequest_IdAndStatus(Long partyRequestId, OfferStatus status);
}
