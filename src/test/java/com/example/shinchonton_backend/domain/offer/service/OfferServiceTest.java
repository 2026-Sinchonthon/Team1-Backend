package com.example.shinchonton_backend.domain.offer.service;

import com.example.shinchonton_backend.domain.deal.repository.DealRepository;
import com.example.shinchonton_backend.domain.member.entity.Member;
import com.example.shinchonton_backend.domain.member.entity.MemberRole;
import com.example.shinchonton_backend.domain.member.repository.MemberRepository;
import com.example.shinchonton_backend.domain.offer.dto.req.OfferCreateReq;
import com.example.shinchonton_backend.domain.offer.dto.res.DealRes;
import com.example.shinchonton_backend.domain.offer.dto.res.OfferDetailRes;
import com.example.shinchonton_backend.domain.offer.dto.res.OfferRes;
import com.example.shinchonton_backend.domain.offer.entity.Offer;
import com.example.shinchonton_backend.domain.offer.entity.OfferStatus;
import com.example.shinchonton_backend.domain.offer.repository.OfferRepository;
import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequestStatus;
import com.example.shinchonton_backend.domain.partyrequest.repository.PartyRequestRepository;
import com.example.shinchonton_backend.domain.store.entity.Region;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.domain.store.repository.StoreRepository;
import com.example.shinchonton_backend.global.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class OfferServiceTest {

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private PartyRequestRepository partyRequestRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DealRepository dealRepository;

    @BeforeEach
    void setUp() {
        dealRepository.deleteAll();
        offerRepository.deleteAll();
        partyRequestRepository.deleteAll();
        storeRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void createOffer_savesPendingOffer() {
        PartyRequest partyRequest = savePartyRequest();
        Store store = saveStore("merchant1", "신촌 테스트포차");

        OfferRes response = offerService.createOffer(
                partyRequest.getId(),
                new OfferCreateReq(store.getId(), 280_000, 10, "기본 안주 1개 서비스", "단체석 준비 가능합니다.")
        );

        assertThat(response.offerId()).isNotNull();
        assertThat(response.requestId()).isEqualTo(partyRequest.getId());
        assertThat(response.storeId()).isEqualTo(store.getId());
        assertThat(response.status()).isEqualTo(OfferStatus.PENDING);
        assertThat(response.discountRate()).isEqualTo(10);
    }

    @Test
    void createOffer_rejectsDuplicateOfferFromSameStore() {
        PartyRequest partyRequest = savePartyRequest();
        Store store = saveStore("merchant1", "신촌 테스트포차");
        OfferCreateReq request = new OfferCreateReq(store.getId(), 280_000, 10, null, "첫 번째 제안입니다.");

        offerService.createOffer(partyRequest.getId(), request);

        assertThatThrownBy(() -> offerService.createOffer(partyRequest.getId(), request))
                .isInstanceOf(GeneralException.class);
    }

    @Test
    void getOffer_returnsDetailForSelectedOffer() {
        PartyRequest partyRequest = savePartyRequest();
        Store store = saveStore("merchant1", "신촌 테스트포차");
        OfferRes createdOffer = offerService.createOffer(
                partyRequest.getId(),
                new OfferCreateReq(
                        store.getId(),
                        270_000,
                        10,
                        "음료 서비스",
                        "단체석 준비 가능합니다."
                )
        );

        OfferDetailRes result = offerService.getOffer(createdOffer.offerId());

        assertThat(result.offerId()).isEqualTo(createdOffer.offerId());
        assertThat(result.requestId()).isEqualTo(partyRequest.getId());
        assertThat(result.storeId()).isEqualTo(store.getId());
        assertThat(result.storeName()).isEqualTo("신촌 테스트포차");
        assertThat(result.message()).isEqualTo("단체석 준비 가능합니다.");
        assertThat(result.benefitDescription()).isEqualTo("음료 서비스");
        assertThat(result.tableCount()).isEqualTo(6);
        assertThat(result.perTableOfferedPrice()).isEqualTo(45_000);
        assertThat(result.offeredTotalPrice()).isEqualTo(270_000);
        assertThat(result.status()).isEqualTo(OfferStatus.PENDING);
    }

    @Test
    void getOffer_throwsWhenOfferDoesNotExist() {
        assertThatThrownBy(() -> offerService.getOffer(999L))
                .isInstanceOf(GeneralException.class);
    }

    @Test
    void acceptOffer_createsDealAndRejectsOtherPendingOffers() {
        PartyRequest partyRequest = savePartyRequest();
        Store acceptedStore = saveStore("merchant1", "신촌 테스트포차");
        Store rejectedStore = saveStore("merchant2", "이대 테스트포차");

        OfferRes acceptedOffer = offerService.createOffer(
                partyRequest.getId(),
                new OfferCreateReq(acceptedStore.getId(), 270_000, 10, "음료 서비스", "좋은 자리 준비 가능합니다.")
        );
        OfferRes otherOffer = offerService.createOffer(
                partyRequest.getId(),
                new OfferCreateReq(rejectedStore.getId(), 260_000, 15, "안주 서비스", "대형룸 가능합니다.")
        );

        DealRes deal = offerService.acceptOffer(acceptedOffer.offerId());

        PartyRequest matchedRequest = partyRequestRepository.findById(partyRequest.getId()).orElseThrow();
        Offer selected = offerRepository.findById(acceptedOffer.offerId()).orElseThrow();
        Offer rejected = offerRepository.findById(otherOffer.offerId()).orElseThrow();

        assertThat(deal.dealId()).isNotNull();
        assertThat(matchedRequest.getStatus()).isEqualTo(PartyRequestStatus.MATCHED);
        assertThat(selected.getStatus()).isEqualTo(OfferStatus.ACCEPTED);
        assertThat(rejected.getStatus()).isEqualTo(OfferStatus.REJECTED);
    }

    @Test
    void getOffers_throwsWhenRequestDoesNotExist() {
        assertThatThrownBy(() -> offerService.getOffers(999L))
                .isInstanceOf(GeneralException.class);
    }

    private PartyRequest savePartyRequest() {
        Member student = memberRepository.save(Member.register(
                "student1",
                "{noop}password",
                MemberRole.STUDENT,
                "테스트 학생",
                "010-0000-0000"
        ));

        return partyRequestRepository.save(PartyRequest.open(
                student,
                "멋쟁이사자처럼",
                "해커톤 뒤풀이",
                24,
                LocalDateTime.now().plusDays(7),
                Region.SINCHON,
                300_000,
                200_000,
                "조용한 자리 선호"
        ));
    }

    private Store saveStore(String loginId, String storeName) {
        Member merchant = memberRepository.save(Member.register(
                loginId,
                "{noop}password",
                MemberRole.MERCHANT,
                storeName + " 사장님",
                "010-1111-1111"
        ));

        return storeRepository.save(Store.register(
                merchant,
                storeName,
                Region.SINCHON,
                "서울 서대문구 신촌로 1",
                80,
                LocalTime.of(17, 0),
                LocalTime.of(2, 0),
                "테스트용 가게"
        ));
    }
}
