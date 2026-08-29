package com.example.shinchonton_backend.domain.offer.dto.res;

import com.example.shinchonton_backend.domain.offer.entity.Offer;
import com.example.shinchonton_backend.domain.offer.entity.OfferStatus;

public record OfferDetailRes(
        Long offerId,
        Long requestId,
        Long storeId,
        String storeName,
        String message,
        String benefitDescription,
        int tableCount,
        long perTableOfferedPrice,
        long offeredTotalPrice,
        OfferStatus status
) {

    public static OfferDetailRes from(Offer offer) {
        int tableCount = offer.getPartyRequest().getTableCount();

        return new OfferDetailRes(
                offer.getId(),
                offer.getPartyRequest().getId(),
                offer.getStore().getId(),
                offer.getStore().getName(),
                offer.getMessage(),
                offer.getBenefitDescription(),
                tableCount,
                offer.getOfferedTotalPrice() / tableCount,
                offer.getOfferedTotalPrice(),
                offer.getStatus()
        );
    }
}
