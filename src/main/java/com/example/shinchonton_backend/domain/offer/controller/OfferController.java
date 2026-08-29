package com.example.shinchonton_backend.domain.offer.controller;

import com.example.shinchonton_backend.domain.offer.dto.req.OfferCreateReq;
import com.example.shinchonton_backend.domain.offer.dto.res.DealRes;
import com.example.shinchonton_backend.domain.offer.dto.res.OfferRes;
import com.example.shinchonton_backend.domain.offer.service.OfferService;
import com.example.shinchonton_backend.global.common.ApiResponse;
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
public class OfferController {

    private final OfferService offerService;

    @PostMapping("/requests/{requestId}/offers")
    public ResponseEntity<ApiResponse<OfferRes>> createOffer(
            @PathVariable Long requestId,
            @Valid @RequestBody OfferCreateReq request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(offerService.createOffer(requestId, request), "제안이 발송되었습니다."));
    }

    @GetMapping("/requests/{requestId}/offers")
    public ApiResponse<List<OfferRes>> getOffers(@PathVariable Long requestId) {
        return ApiResponse.success(offerService.getOffers(requestId));
    }

    @PostMapping("/offers/{offerId}/accept")
    public ApiResponse<DealRes> acceptOffer(@PathVariable Long offerId) {
        return ApiResponse.success(offerService.acceptOffer(offerId), "제안이 수락되어 체결되었습니다.");
    }
}
