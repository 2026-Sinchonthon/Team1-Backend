package com.example.shinchonton_backend.domain.store.controller;

import com.example.shinchonton_backend.domain.store.dto.StoreRequestDto;
import com.example.shinchonton_backend.domain.store.dto.StoreResponseDto;
import com.example.shinchonton_backend.domain.store.service.MenuCommandService;
import com.example.shinchonton_backend.domain.store.service.StoreCommandService;
import com.example.shinchonton_backend.domain.store.service.StoreQueryService;
import com.example.shinchonton_backend.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreCommandService storeCommandService;
    private final MenuCommandService menuCommandService;
    private final StoreQueryService storeQueryService;

    /**
     * 가게 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponseDto.Create>> registerStore(
            @RequestHeader("X-Member-Id") Long memberId,
            @Valid @RequestBody StoreRequestDto.Create request
    ) {
        StoreResponseDto.Create response =
                storeCommandService.register(memberId, request);

        return ResponseEntity
                .created(URI.create("/stores/" + response.storeId()))
                .body(ApiResponse.onSuccess(
                        "가게가 등록되었습니다.",
                        response
                ));
    }

    /**
     * 메뉴 일괄 등록
     */
    @PostMapping("/{storeId}/menus")
    public ResponseEntity<ApiResponse<StoreResponseDto.MenuBulkCreate>> registerMenus(
            @RequestHeader("X-Member-Id") Long memberId,
            @PathVariable Long storeId,
            @Valid @RequestBody StoreRequestDto.MenuBulkCreate request
    ) {
        StoreResponseDto.MenuBulkCreate response =
                menuCommandService.registerMenus(
                        memberId,
                        storeId,
                        request
                );

        return ResponseEntity
                .created(URI.create("/stores/" + storeId + "/menus"))
                .body(ApiResponse.onSuccess(
                        "메뉴가 등록되었습니다.",
                        response
                ));
    }

    /**
     * 내 가게 목록 조회
     */
    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<StoreResponseDto.Mine>> getMyStores(
            @RequestHeader("X-Member-Id") Long memberId
    ) {
        StoreResponseDto.Mine response =
                storeQueryService.getMyStores(memberId);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(
                        "내 가게 목록을 조회했습니다.",
                        response
                )
        );
    }

    /**
     * 가게 상세 조회
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponseDto.Detail>> getStoreDetail(
            @PathVariable Long storeId
    ) {
        StoreResponseDto.Detail response =
                storeQueryService.getDetail(storeId);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(
                        "가게 정보를 조회했습니다.",
                        response
                )
        );
    }
}
