package com.example.shinchonton_backend.domain.store.service;

import com.example.shinchonton_backend.domain.member.entity.Member;
import com.example.shinchonton_backend.domain.member.entity.MemberRole;
import com.example.shinchonton_backend.domain.member.repository.MemberRepository;
import com.example.shinchonton_backend.domain.store.code.StoreErrorCode;
import com.example.shinchonton_backend.domain.store.dto.StoreResponseDto;
import com.example.shinchonton_backend.domain.store.entity.MenuCategory;
import com.example.shinchonton_backend.domain.store.entity.MenuItem;
import com.example.shinchonton_backend.domain.store.entity.Region;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.domain.store.repository.MenuItemRepository;
import com.example.shinchonton_backend.domain.store.repository.StoreRepository;
import com.example.shinchonton_backend.global.exception.GeneralException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class StoreQueryServiceTest {

    @Autowired
    private StoreQueryService storeQueryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    void 가게_상세에는_품절_메뉴도_함께_반환한다() {
        Member merchant = saveMerchant("merchant-detail");
        Store store = saveStore(merchant, "신촌포차");

        MenuItem availableMenu = MenuItem.create(
                store,
                "후라이드치킨",
                19_000,
                MenuCategory.MAIN,
                2
        );
        MenuItem unavailableMenu = MenuItem.create(
                store,
                "해물탕",
                24_000,
                MenuCategory.SOUP,
                3
        );
        unavailableMenu.markUnavailable();
        menuItemRepository.save(availableMenu);
        menuItemRepository.save(unavailableMenu);

        StoreResponseDto.Detail result =
                storeQueryService.getDetail(store.getId());

        assertThat(result.storeId()).isEqualTo(store.getId());
        assertThat(result.menus()).hasSize(2);
        assertThat(result.menus())
                .extracting(StoreResponseDto.Menu::available)
                .containsExactly(true, false);
    }

    @Test
    void 사장님은_자신의_가게_목록을_조회한다() {
        Member merchant = saveMerchant("merchant-mine");
        Store firstStore = saveStore(merchant, "첫 번째 가게");
        Store secondStore = saveStore(merchant, "두 번째 가게");

        StoreResponseDto.Mine result =
                storeQueryService.getMyStores(merchant.getId());

        assertThat(result.stores()).hasSize(2);
        assertThat(result.stores())
                .extracting(StoreResponseDto.Summary::storeId)
                .containsExactlyInAnyOrder(
                        firstStore.getId(),
                        secondStore.getId()
                );
    }

    @Test
    void 존재하지_않는_가게_상세를_조회하면_예외가_발생한다() {
        assertThatThrownBy(() -> storeQueryService.getDetail(999L))
                .isInstanceOfSatisfying(
                        GeneralException.class,
                        exception -> assertThat(exception.getErrorCode())
                                .isEqualTo(StoreErrorCode.STORE_NOT_FOUND)
                );
    }

    private Member saveMerchant(String loginId) {
        return memberRepository.save(Member.register(
                loginId,
                "encoded-password",
                MemberRole.MERCHANT,
                "테스트 사장님",
                "010-1234-5678"
        ));
    }

    private Store saveStore(Member merchant, String name) {
        return storeRepository.save(Store.register(
                merchant,
                name,
                Region.SINCHON,
                "서울 서대문구 연세로",
                80,
                LocalTime.of(17, 0),
                LocalTime.of(2, 0),
                "단체석 이용 가능"
        ));
    }
}
