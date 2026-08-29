package com.example.shinchonton_backend.domain.deal.entity;

import com.example.shinchonton_backend.domain.offer.entity.Offer;
import com.example.shinchonton_backend.domain.offer.entity.OfferStatus;
import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "deals",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_deals_party_request", columnNames = "party_request_id"),
                @UniqueConstraint(name = "uk_deals_accepted_offer", columnNames = "accepted_offer_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_request_id", nullable = false)
    private PartyRequest partyRequest;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accepted_offer_id", nullable = false)
    private Offer acceptedOffer;

    @Column(name = "matched_at", nullable = false)
    private LocalDateTime matchedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Deal(PartyRequest partyRequest, Offer acceptedOffer, LocalDateTime matchedAt) {
        if (partyRequest == null || partyRequest.getStatus() != PartyRequestStatus.MATCHED) {
            throw new IllegalArgumentException("체결된 리퀘스트가 필요합니다.");
        }
        if (acceptedOffer == null || acceptedOffer.getStatus() != OfferStatus.ACCEPTED) {
            throw new IllegalArgumentException("수락된 제안이 필요합니다.");
        }
        if (acceptedOffer.getPartyRequest() != partyRequest) {
            throw new IllegalArgumentException("제안과 리퀘스트가 일치하지 않습니다.");
        }
        this.partyRequest = partyRequest;
        this.acceptedOffer = acceptedOffer;
        this.matchedAt = matchedAt;
    }

    public static Deal complete(PartyRequest partyRequest, Offer acceptedOffer, LocalDateTime matchedAt) {
        if (matchedAt == null) {
            throw new IllegalArgumentException("체결 시각은 비어 있을 수 없습니다.");
        }
        return new Deal(partyRequest, acceptedOffer, matchedAt);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
