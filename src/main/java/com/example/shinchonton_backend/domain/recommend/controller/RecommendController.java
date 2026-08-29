package com.example.shinchonton_backend.domain.recommend.controller;

import com.example.shinchonton_backend.domain.recommend.dto.req.RecommendReq;
import com.example.shinchonton_backend.domain.recommend.dto.res.ComboRes;
import com.example.shinchonton_backend.domain.recommend.service.RecommendService;
import com.example.shinchonton_backend.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping
    public ApiResponse<List<ComboRes>> recommend(
            @Valid @RequestBody RecommendReq request
    ) {
        return ApiResponse.onSuccess(
                "AI 안주 조합 추천에 성공했습니다.",
                recommendService.recommend(request)
        );
    }
}
