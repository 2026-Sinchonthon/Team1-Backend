package com.example.shinchonton_backend.domain.offer.controller;

import com.example.shinchonton_backend.domain.offer.dto.req.OfferCreateReq;
import com.example.shinchonton_backend.domain.offer.dto.res.DealRes;
import com.example.shinchonton_backend.domain.offer.dto.res.OfferDetailRes;
import com.example.shinchonton_backend.domain.offer.dto.res.OfferRes;
import com.example.shinchonton_backend.domain.offer.service.OfferService;
import com.example.shinchonton_backend.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Offer", description = "가게 제안과 체결 API")
public class OfferController {

    private final OfferService offerService;

    @PostMapping("/requests/{requestId}/offers")
    public ResponseEntity<ApiResponse<OfferRes>> createOffer(
            @PathVariable Long requestId,
            @Valid @RequestBody OfferCreateReq request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess("제안이 발송되었습니다.", offerService.createOffer(requestId, request)));
    }

    @GetMapping("/requests/{requestId}/offers")
    public ApiResponse<List<OfferRes>> getOffers(@PathVariable Long requestId) {
        return ApiResponse.onSuccess("제안 목록 조회에 성공했습니다.", offerService.getOffers(requestId));
    }

    @GetMapping("/offers/{offerId}")
    @Operation(summary = "제안 상세 조회")
    public ApiResponse<OfferDetailRes> getOffer(@PathVariable Long offerId) {
        return ApiResponse.onSuccess("제안 상세 조회에 성공했습니다.", offerService.getOffer(offerId));
    }

    @PostMapping("/offers/{offerId}/accept")
    public ApiResponse<DealRes> acceptOffer(@PathVariable Long offerId) {
        return ApiResponse.onSuccess("제안이 수락되어 체결되었습니다.", offerService.acceptOffer(offerId));
    }
}
