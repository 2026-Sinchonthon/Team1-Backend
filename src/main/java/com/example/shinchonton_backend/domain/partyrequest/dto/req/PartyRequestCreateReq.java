package com.example.shinchonton_backend.domain.partyrequest.dto.req;

import com.example.shinchonton_backend.domain.store.entity.Region;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PartyRequestCreateReq(
        @NotNull(message = "학생 ID는 필수입니다.")
        @Positive(message = "학생 ID는 1 이상이어야 합니다.")
        Long studentId,

        @NotBlank(message = "단체명은 필수입니다.")
        @Size(max = 30, message = "단체명은 30자 이하여야 합니다.")
        String groupName,

        @NotBlank(message = "단체 목적은 필수입니다.")
        @Size(max = 30, message = "단체 목적은 30자 이하여야 합니다.")
        String purpose,

        @NotNull(message = "인원은 필수입니다.")
        @Min(value = 10, message = "인원은 10명 이상이어야 합니다.")
        @Max(value = 300, message = "인원은 300명 이하여야 합니다.")
        Integer headcount,

        @NotNull(message = "예약 일시는 필수입니다.")
        @Future(message = "예약 일시는 현재 이후여야 합니다.")
        LocalDateTime reservedAt,

        Region preferredRegion,

        @NotNull(message = "총예산은 필수입니다.")
        @Min(value = 10_000, message = "총예산은 10,000원 이상이어야 합니다.")
        Long totalBudget,

        @NotNull(message = "기본 안주 예산은 필수입니다.")
        @Positive(message = "기본 안주 예산은 0원보다 커야 합니다.")
        Long baseFoodBudget,

        @Size(max = 500, message = "요청 사항은 500자 이하여야 합니다.")
        String note
) {
}
