package com.example.shinchonton_backend.domain.partyrequest.controller;

import com.example.shinchonton_backend.domain.partyrequest.dto.req.PartyRequestCreateReq;
import com.example.shinchonton_backend.domain.partyrequest.dto.res.PartyRequestCreateRes;
import com.example.shinchonton_backend.domain.partyrequest.dto.res.PartyRequestDetailRes;
import com.example.shinchonton_backend.domain.partyrequest.dto.res.PartyRequestSummaryRes;
import com.example.shinchonton_backend.domain.partyrequest.service.PartyRequestService;
import com.example.shinchonton_backend.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Tag(name = "PartyRequest", description = "단체 예약 리퀘스트 API")
public class PartyRequestController {

    private final PartyRequestService partyRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<PartyRequestCreateRes>> create(
            @Valid @RequestBody PartyRequestCreateReq request
    ) {
        PartyRequestCreateRes response = partyRequestService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess("리퀘스트를 등록했습니다.", response));
    }

    @GetMapping
    public ApiResponse<List<PartyRequestSummaryRes>> findAvailableForStore(
            @RequestParam @Positive Long storeId
    ) {
        return ApiResponse.onSuccess(
                "제안 가능한 리퀘스트를 조회했습니다.",
                partyRequestService.findAvailableForStore(storeId)
        );
    }

    @GetMapping("/mine")
    public ApiResponse<List<PartyRequestSummaryRes>> findMine(
            @RequestParam @Positive Long studentId
    ) {
        return ApiResponse.onSuccess(
                "내 리퀘스트를 조회했습니다.",
                partyRequestService.findMine(studentId)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<PartyRequestDetailRes> findDetail(
            @PathVariable @Positive Long id
    ) {
        return ApiResponse.onSuccess(
                "리퀘스트 상세를 조회했습니다.",
                partyRequestService.findDetail(id)
        );
    }
}
