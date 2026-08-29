package com.example.shinchonton_backend.domain.recommend.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecommendReq(
        @NotNull(message = "가게 ID는 필수입니다.")
        @Positive(message = "가게 ID는 1 이상이어야 합니다.")
        Long storeId,

        @NotNull(message = "리퀘스트 ID는 필수입니다.")
        @Positive(message = "리퀘스트 ID는 1 이상이어야 합니다.")
        Long partyRequestId,

        @Min(value = 1000, message = "테이블당 예산은 1,000원 이상이어야 합니다.")
        long tableBudget
) {
}
