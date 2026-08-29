package com.example.shinchonton_backend.domain.recommend.dto.res;

import java.util.List;

public record ComboRes(
        String name,
        List<ComboItemRes> items,
        long totalPrice
) {
}
