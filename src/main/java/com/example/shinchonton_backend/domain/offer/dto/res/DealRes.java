package com.example.shinchonton_backend.domain.offer.dto.res;

import com.example.shinchonton_backend.domain.deal.entity.Deal;

import java.time.LocalDateTime;

public record DealRes(
        Long dealId,
        Long requestId,
        Long acceptedOfferId,
        Long storeId,
        String storeName,
        String storePhone,
        String studentName,
        String studentPhone,
        long finalPrice,
        LocalDateTime matchedAt
) {

    public static DealRes from(Deal deal) {
        return new DealRes(
                deal.getId(),
                deal.getPartyRequest().getId(),
                deal.getAcceptedOffer().getId(),
                deal.getAcceptedOffer().getStore().getId(),
                deal.getAcceptedOffer().getStore().getName(),
                deal.getAcceptedOffer().getStore().getMerchant().getPhone(),
                deal.getPartyRequest().getStudent().getName(),
                deal.getPartyRequest().getStudent().getPhone(),
                deal.getAcceptedOffer().getOfferedTotalPrice(),
                deal.getMatchedAt()
        );
    }
}
