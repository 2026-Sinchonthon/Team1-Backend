package com.example.shinchonton_backend.domain.store.service;

import com.example.shinchonton_backend.domain.store.code.StoreErrorCode;
import com.example.shinchonton_backend.domain.store.dto.StoreRequestDto;
import com.example.shinchonton_backend.domain.store.dto.StoreResponseDto;
import com.example.shinchonton_backend.domain.store.entity.MenuItem;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.domain.store.repository.MenuItemRepository;
import com.example.shinchonton_backend.domain.store.repository.StoreRepository;
import com.example.shinchonton_backend.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MenuCommandService {

    private final StoreRepository storeRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public StoreResponseDto.MenuBulkCreate registerMenus(
            Long memberId,
            Long storeId,
            StoreRequestDto.MenuBulkCreate request
    ) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new GeneralException(
                        StoreErrorCode.STORE_NOT_FOUND
                ));

        validateOwner(memberId, store);
        validateDuplicateMenuNames(storeId, request);

        List<MenuItem> menuItems = request.menus()
                .stream()
                .map(menu -> MenuItem.create(
                        store,
                        menu.name().trim(),
                        menu.price(),
                        menu.category(),
                        menu.servingSize()
                ))
                .toList();

        List<MenuItem> savedMenuItems =
                menuItemRepository.saveAll(menuItems);

        return StoreResponseDto.MenuBulkCreate.of(
                store.getId(),
                savedMenuItems
        );
    }

    private void validateOwner(Long memberId, Store store) {
        if (!store.getMerchant().getId().equals(memberId)) {
            throw new GeneralException(
                    StoreErrorCode.STORE_ACCESS_DENIED
            );
        }
    }

    private void validateDuplicateMenuNames(
            Long storeId,
            StoreRequestDto.MenuBulkCreate request
    ) {
        Set<String> existingNames = menuItemRepository
                .findAllByStoreId(storeId)
                .stream()
                .map(MenuItem::getName)
                .map(this::normalize)
                .collect(java.util.stream.Collectors.toSet());

        Set<String> requestNames = new HashSet<>();

        for (StoreRequestDto.MenuCreate menu : request.menus()) {
            String normalizedName = normalize(menu.name());

            if (existingNames.contains(normalizedName)
                    || !requestNames.add(normalizedName)) {
                throw new GeneralException(
                        StoreErrorCode.DUPLICATE_MENU_NAME
                );
            }
        }
    }

    private String normalize(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}