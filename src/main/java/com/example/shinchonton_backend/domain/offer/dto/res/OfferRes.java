package com.example.shinchonton_backend.domain.offer.dto.res;

import com.example.shinchonton_backend.domain.offer.entity.Offer;
import com.example.shinchonton_backend.domain.offer.entity.OfferStatus;

import java.time.LocalDateTime;

public record OfferRes(
//        제안 정보를 프론트에 보내는 응답 dto
        Long offerId,
        Long requestId,
        Long storeId,
        String storeName,
        int storeMaxCapacity,
        long offeredTotalPrice,
        long originalTotalBudget,
        int discountRate,
        boolean overBudget,
        String benefitDescription,
        String message,
        OfferStatus status,
        LocalDateTime acceptedAt
) {

    public static OfferRes from(Offer offer) {
        long originalBudget = offer.getPartyRequest().getTotalBudget();

        return new OfferRes(
                offer.getId(),
                offer.getPartyRequest().getId(),
                offer.getStore().getId(),
                offer.getStore().getName(),
                offer.getStore().getMaxCapacity(),
                offer.getOfferedTotalPrice(),
                originalBudget,
                offer.getDiscountRate(),
                offer.isOverBudget(),
                offer.getBenefitDescription(),
                offer.getMessage(),
                offer.getStatus(),
                offer.getAcceptedAt()
        );
    }
}
