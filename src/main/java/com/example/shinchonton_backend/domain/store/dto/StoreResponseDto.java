package com.example.shinchonton_backend.domain.store.dto;

import com.example.shinchonton_backend.domain.store.entity.MenuCategory;
import com.example.shinchonton_backend.domain.store.entity.MenuItem;
import com.example.shinchonton_backend.domain.store.entity.Region;
import com.example.shinchonton_backend.domain.store.entity.Store;

import java.time.LocalTime;
import java.util.List;

public final class StoreResponseDto {

    private StoreResponseDto() {
    }

    public record Create(
            Long storeId,
            String name,
            Region region,
            String address,
            int maxCapacity,
            LocalTime openTime,
            LocalTime closeTime,
            String description
    ) {
        public static Create from(Store store) {
            return new Create(
                    store.getId(),
                    store.getName(),
                    store.getRegion(),
                    store.getAddress(),
                    store.getMaxCapacity(),
                    store.getOpenTime(),
                    store.getCloseTime(),
                    store.getDescription()
            );
        }
    }

    public record MenuBulkCreate(
            Long storeId,
            int createdCount,
            List<Menu> menus
    ) {
        public static MenuBulkCreate of(
                Long storeId,
                List<MenuItem> menuItems
        ) {
            List<Menu> menus = menuItems.stream()
                    .map(Menu::from)
                    .toList();

            return new MenuBulkCreate(
                    storeId,
                    menus.size(),
                    menus
            );
        }
    }

    public record Menu(
            Long menuId,
            String name,
            long price,
            MenuCategory category,
            Integer servingSize,
            boolean available
    ) {
        public static Menu from(MenuItem menuItem) {
            return new Menu(
                    menuItem.getId(),
                    menuItem.getName(),
                    menuItem.getPrice(),
                    menuItem.getCategory(),
                    menuItem.getServingSize(),
                    menuItem.isAvailable()
            );
        }
    }
}