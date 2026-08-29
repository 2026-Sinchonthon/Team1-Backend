package com.example.shinchonton_backend.domain.store.entity;

import com.example.shinchonton_backend.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "menu_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private long price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MenuCategory category;

    @Column(name = "serving_size")
    private Integer servingSize;

    @Column(nullable = false)
    private boolean available;

    private MenuItem(Store store, String name, long price, MenuCategory category, Integer servingSize) {
        if (store == null) {
            throw new IllegalArgumentException("가게는 비어 있을 수 없습니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("메뉴 이름은 비어 있을 수 없습니다.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("메뉴 가격은 0원보다 커야 합니다.");
        }
        if (category == null) {
            throw new IllegalArgumentException("메뉴 카테고리는 비어 있을 수 없습니다.");
        }
        if (servingSize != null && servingSize <= 0) {
            throw new IllegalArgumentException("권장 인원은 1명 이상이어야 합니다.");
        }
        this.store = store;
        this.name = name;
        this.price = price;
        this.category = category;
        this.servingSize = servingSize;
        this.available = true;
    }

    public static MenuItem create(
            Store store,
            String name,
            long price,
            MenuCategory category,
            Integer servingSize
    ) {
        return new MenuItem(store, name, price, category, servingSize);
    }

    public void markAvailable() {
        this.available = true;
    }

    public void markUnavailable() {
        this.available = false;
    }
}
