package com.example.shinchonton_backend.domain.offer.entity;

import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "offers",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_offers_request_store",
                columnNames = {"party_request_id", "store_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Offer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_request_id", nullable = false)
    private PartyRequest partyRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "offered_total_price", nullable = false)
    private long offeredTotalPrice;

    @Column(name = "discount_rate", nullable = false)
    private int discountRate;

    @Column(name = "benefit_description", length = 255)
    private String benefitDescription;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OfferStatus status;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    private Offer(
            PartyRequest partyRequest,
            Store store,
            long offeredTotalPrice,
            int discountRate,
            String benefitDescription,
            String message
    ) {
        if (partyRequest == null) {
            throw new IllegalArgumentException("리퀘스트는 비어 있을 수 없습니다.");
        }
        if (store == null) {
            throw new IllegalArgumentException("가게는 비어 있을 수 없습니다.");
        }
        if (offeredTotalPrice <= 0) {
            throw new IllegalArgumentException("제안 총액은 0원보다 커야 합니다.");
        }
        if (discountRate < 0 || discountRate > 100) {
            throw new IllegalArgumentException("할인율은 0 이상 100 이하여야 합니다.");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("제안 메시지는 비어 있을 수 없습니다.");
        }
        if (!store.canAccommodate(partyRequest.getHeadcount())) {
            throw new IllegalArgumentException("해당 가게는 요청 인원을 수용할 수 없습니다.");
        }
        if (!partyRequest.isOpenAt(LocalDateTime.now())) {
            throw new IllegalStateException("모집 중인 리퀘스트에만 제안할 수 있습니다.");
        }
        this.partyRequest = partyRequest;
        this.store = store;
        this.offeredTotalPrice = offeredTotalPrice;
        this.discountRate = discountRate;
        this.benefitDescription = benefitDescription;
        this.message = message;
        this.status = OfferStatus.PENDING;
    }

    public static Offer propose(
            PartyRequest partyRequest,
            Store store,
            long offeredTotalPrice,
            int discountRate,
            String benefitDescription,
            String message
    ) {
        return new Offer(partyRequest, store, offeredTotalPrice, discountRate, benefitDescription, message);
    }

    public boolean isOverBudget() {
        return offeredTotalPrice > partyRequest.getTotalBudget();
    }

    public void accept(LocalDateTime acceptedAt) {
        if (status != OfferStatus.PENDING) {
            throw new IllegalStateException("대기 중인 제안만 수락할 수 있습니다.");
        }
        this.status = OfferStatus.ACCEPTED;
        this.acceptedAt = acceptedAt;
    }

    public void reject() {
        if (status != OfferStatus.PENDING) {
            throw new IllegalStateException("대기 중인 제안만 거절할 수 있습니다.");
        }
        this.status = OfferStatus.REJECTED;
    }
}
