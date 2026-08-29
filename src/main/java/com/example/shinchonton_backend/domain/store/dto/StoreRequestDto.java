package com.example.shinchonton_backend.domain.store.dto;

import com.example.shinchonton_backend.domain.store.entity.MenuCategory;
import com.example.shinchonton_backend.domain.store.entity.Region;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;
import java.util.List;

public final class StoreRequestDto {

    private StoreRequestDto() {
    }

    public record Create(

            @NotBlank(message = "가게 이름은 필수입니다.")
            @Size(max = 100, message = "가게 이름은 100자 이하여야 합니다.")
            String name,

            @NotNull(message = "지역은 필수입니다.")
            Region region,

            @NotBlank(message = "주소는 필수입니다.")
            @Size(max = 255, message = "주소는 255자 이하여야 합니다.")
            String address,

            @Positive(message = "최대 수용 인원은 1명 이상이어야 합니다.")
            int maxCapacity,

            LocalTime openTime,

            LocalTime closeTime,

            @Size(max = 500, message = "가게 설명은 500자 이하여야 합니다.")
            String description
    ) {
    }

    public record MenuBulkCreate(

            @NotEmpty(message = "등록할 메뉴가 하나 이상 필요합니다.")
            @Size(max = 100, message = "메뉴는 한 번에 최대 100개까지 등록할 수 있습니다.")
            List<@Valid MenuCreate> menus
    ) {
    }

    /**
     * 일괄 등록 메뉴 한 건
     */
    public record MenuCreate(

            @NotBlank(message = "메뉴 이름은 필수입니다.")
            @Size(max = 100, message = "메뉴 이름은 100자 이하여야 합니다.")
            String name,

            @Positive(message = "메뉴 가격은 0원보다 커야 합니다.")
            long price,

            @NotNull(message = "메뉴 카테고리는 필수입니다.")
            MenuCategory category,

            @Positive(message = "권장 인원은 1명 이상이어야 합니다.")
            Integer servingSize
    ) {
    }
}