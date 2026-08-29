package com.example.shinchonton_backend.domain.store.service;

import com.example.shinchonton_backend.domain.member.code.MemberErrorCode;
import com.example.shinchonton_backend.domain.member.entity.Member;
import com.example.shinchonton_backend.domain.member.repository.MemberRepository;
import com.example.shinchonton_backend.domain.store.code.StoreErrorCode;
import com.example.shinchonton_backend.domain.store.dto.StoreRequestDto;
import com.example.shinchonton_backend.domain.store.dto.StoreResponseDto;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.domain.store.repository.StoreRepository;
import com.example.shinchonton_backend.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreCommandService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public StoreResponseDto.Create register(
            Long memberId,
            StoreRequestDto.Create request
    ) {
        Member merchant = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(
                        MemberErrorCode.MEMBER_NOT_FOUND
                ));

        if (!merchant.isMerchant()) {
            throw new GeneralException(StoreErrorCode.MERCHANT_REQUIRED);
        }

        Store store = Store.register(
                merchant,
                request.name(),
                request.region(),
                request.address(),
                request.maxCapacity(),
                request.openTime(),
                request.closeTime(),
                request.description()
        );

        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.Create.from(savedStore);
    }
}
