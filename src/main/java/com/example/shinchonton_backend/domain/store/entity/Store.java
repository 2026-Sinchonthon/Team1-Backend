package com.example.shinchonton_backend.domain.store.entity;

import com.example.shinchonton_backend.domain.member.entity.Member;
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

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "stores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Member merchant;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Region region;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(length = 500)
    private String description;

    private Store(
            Member merchant,
            String name,
            Region region,
            String address,
            int maxCapacity,
            LocalTime openTime,
            LocalTime closeTime,
            String description
    ) {
        if (merchant == null || !merchant.isMerchant()) {
            throw new IllegalArgumentException("사장님 회원만 가게를 등록할 수 있습니다.");
        }
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("최대 수용 인원은 1명 이상이어야 합니다.");
        }
        this.merchant = merchant;
        this.name = requireText(name, "가게 이름");
        this.region = requireNonNull(region, "지역");
        this.address = requireText(address, "주소");
        this.maxCapacity = maxCapacity;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.description = description;
    }

    public static Store register(
            Member merchant,
            String name,
            Region region,
            String address,
            int maxCapacity,
            LocalTime openTime,
            LocalTime closeTime,
            String description
    ) {
        return new Store(merchant, name, region, address, maxCapacity, openTime, closeTime, description);
    }

    public boolean canAccommodate(int headcount) {
        return headcount > 0 && maxCapacity >= headcount;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + "은(는) 비어 있을 수 없습니다.");
        }
        return value;
    }

    private static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + "은(는) 비어 있을 수 없습니다.");
        }
        return value;
    }
}
