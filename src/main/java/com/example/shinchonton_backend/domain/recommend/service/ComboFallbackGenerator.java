package com.example.shinchonton_backend.domain.recommend.service;

import com.example.shinchonton_backend.domain.recommend.dto.res.ComboItemRes;
import com.example.shinchonton_backend.domain.recommend.dto.res.ComboRes;
import com.example.shinchonton_backend.domain.store.entity.MenuItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class ComboFallbackGenerator {

    public List<ComboRes> generate(List<MenuItem> menus, long tableBudget) {
        List<MenuItem> sortedMenus = menus.stream()
                .sorted(Comparator.comparingLong(MenuItem::getPrice))
                .toList();

        List<ComboItemRes> items = new ArrayList<>();
        long totalPrice = 0;

        for (MenuItem menu : sortedMenus) {
            if (totalPrice + menu.getPrice() <= tableBudget) {
                items.add(new ComboItemRes(
                        menu.getId(),
                        menu.getName(),
                        menu.getPrice(),
                        1
                ));
                totalPrice += menu.getPrice();
            }
        }

        return List.of(
                new ComboRes(
                        "예산 맞춤 기본 세트",
                        items,
                        totalPrice
                )
        );
    }
}
