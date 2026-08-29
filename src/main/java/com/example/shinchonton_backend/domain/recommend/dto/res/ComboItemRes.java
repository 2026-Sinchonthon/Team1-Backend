package com.example.shinchonton_backend.domain.recommend.dto.res;

public record ComboItemRes(
        Long menuId,
        String menuName,
        long price,
        int quantity
) {
}
