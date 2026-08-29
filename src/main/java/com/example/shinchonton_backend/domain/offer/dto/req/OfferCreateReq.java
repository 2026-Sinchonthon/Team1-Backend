package com.example.shinchonton_backend.domain.offer.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OfferCreateReq(
        @NotNull(message = "가게 ID는 필수입니다.")
        Long storeId,

        @Min(value = 1, message = "제안 총액은 1원 이상이어야 합니다.")
        long offeredTotalPrice,

        @Min(value = 0, message = "할인율은 0 이상이어야 합니다.")
        @Max(value = 100, message = "할인율은 100 이하여야 합니다.")
        int discountRate,

        @Size(max = 255, message = "혜택 설명은 255자 이하여야 합니다.")
        String benefitDescription,

        @NotBlank(message = "제안 메시지는 필수입니다.")
        @Size(max = 500, message = "제안 메시지는 500자 이하여야 합니다.")
        String message
) {
}
