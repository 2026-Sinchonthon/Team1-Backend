package com.example.shinchonton_backend.domain.store.service;

import com.example.shinchonton_backend.domain.member.code.MemberErrorCode;
import com.example.shinchonton_backend.domain.member.entity.Member;
import com.example.shinchonton_backend.domain.member.repository.MemberRepository;
import com.example.shinchonton_backend.domain.store.code.StoreErrorCode;
import com.example.shinchonton_backend.domain.store.dto.StoreResponseDto;
import com.example.shinchonton_backend.domain.store.entity.MenuItem;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.domain.store.repository.MenuItemRepository;
import com.example.shinchonton_backend.domain.store.repository.StoreRepository;
import com.example.shinchonton_backend.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreQueryService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final MenuItemRepository menuItemRepository;

    public StoreResponseDto.Mine getMyStores(Long memberId) {
        Member merchant = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(
                        MemberErrorCode.MEMBER_NOT_FOUND
                ));

        if (!merchant.isMerchant()) {
            throw new GeneralException(
                    StoreErrorCode.MERCHANT_REQUIRED
            );
        }

        List<Store> stores = storeRepository
                .findAllByMerchantIdOrderByCreatedAtDesc(memberId);

        return StoreResponseDto.Mine.from(stores);
    }

    public StoreResponseDto.Detail getDetail(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new GeneralException(
                        StoreErrorCode.STORE_NOT_FOUND
                ));

        List<MenuItem> menuItems = menuItemRepository
                .findAllByStoreIdOrderByIdAsc(storeId);

        return StoreResponseDto.Detail.of(store, menuItems);
    }
}
