package com.example.shinchonton_backend.domain.offer.service;

import com.example.shinchonton_backend.domain.deal.entity.Deal;
import com.example.shinchonton_backend.domain.deal.repository.DealRepository;
import com.example.shinchonton_backend.domain.offer.dto.req.OfferCreateReq;
import com.example.shinchonton_backend.domain.offer.dto.res.DealRes;
import com.example.shinchonton_backend.domain.offer.dto.res.OfferDetailRes;
import com.example.shinchonton_backend.domain.offer.dto.res.OfferRes;
import com.example.shinchonton_backend.domain.offer.entity.Offer;
import com.example.shinchonton_backend.domain.offer.entity.OfferStatus;
import com.example.shinchonton_backend.domain.offer.repository.OfferRepository;
import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.partyrequest.repository.PartyRequestRepository;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.domain.store.repository.StoreRepository;
import com.example.shinchonton_backend.global.apiPayload.code.status.GeneralErrorCode;
import com.example.shinchonton_backend.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfferService {

    private final OfferRepository offerRepository;
    private final PartyRequestRepository partyRequestRepository;
    private final StoreRepository storeRepository;
    private final DealRepository dealRepository;

    @Transactional
    public OfferRes createOffer(Long requestId, OfferCreateReq request) {
        // 제안 생성 중 학생이 다른 제안을 수락할 수 있으므로 리퀘스트를 잠그고 상태를 확인한다.
        PartyRequest partyRequest = partyRequestRepository.findByIdForUpdate(requestId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        if (offerRepository.existsByPartyRequest_IdAndStore_Id(requestId, request.storeId())) {
            throw new GeneralException(GeneralErrorCode.BAD_REQUEST);
        }

        Offer offer = Offer.propose(
                partyRequest,
                store,
                request.offeredTotalPrice(),
                request.discountRate(),
                request.benefitDescription(),
                request.message()
        );

        return OfferRes.from(offerRepository.save(offer));
    }

    public List<OfferRes> getOffers(Long requestId) {
        if (!partyRequestRepository.existsById(requestId)) {
            throw new GeneralException(GeneralErrorCode.NOT_FOUND);
        }

        return offerRepository.findAllByPartyRequest_IdOrderByCreatedAtDesc(requestId)
                .stream()
                .map(OfferRes::from)
                .toList();
    }

    public OfferDetailRes getOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        return OfferDetailRes.from(offer);
    }

    @Transactional
    public DealRes acceptOffer(Long offerId) {
        Offer acceptedOffer = offerRepository.findById(offerId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        // 같은 리퀘스트의 제안이 동시에 수락되는 것을 막기 위해 리퀘스트 행을 잠근다.
        PartyRequest partyRequest = partyRequestRepository.findByIdForUpdate(acceptedOffer.getPartyRequest().getId())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        if (dealRepository.existsByPartyRequest_Id(partyRequest.getId())) {
            throw new GeneralException(GeneralErrorCode.BAD_REQUEST);
        }

        LocalDateTime now = LocalDateTime.now();
        acceptedOffer.accept(now);
        partyRequest.match(now);

        rejectOtherPendingOffers(partyRequest.getId(), acceptedOffer.getId());
//        선택한 제안 말고 같은 리퀘스트에 달린 다른 제안들을 모두 거절 처리한다.

        Deal deal = Deal.complete(partyRequest, acceptedOffer, now);
        return DealRes.from(dealRepository.save(deal));
    }

    private void rejectOtherPendingOffers(Long requestId, Long acceptedOfferId) {
        List<Offer> pendingOffers = offerRepository.findAllByPartyRequest_IdAndStatus(requestId, OfferStatus.PENDING);

        pendingOffers.stream()
                .filter(offer -> !offer.getId().equals(acceptedOfferId))
                .forEach(Offer::reject);
    }
}
