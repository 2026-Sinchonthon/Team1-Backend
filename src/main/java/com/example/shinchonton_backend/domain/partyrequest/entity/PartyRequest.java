package com.example.shinchonton_backend.domain.partyrequest.entity;

import com.example.shinchonton_backend.domain.member.entity.Member;
import com.example.shinchonton_backend.domain.store.entity.Region;
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

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "party_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyRequest extends BaseTimeEntity {

    private static final int PEOPLE_PER_TABLE = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Member student;

    @Column(name = "group_name", nullable = false, length = 30)
    private String groupName;

    @Column(nullable = false, length = 30)
    private String purpose;

    @Column(nullable = false)
    private int headcount;

    @Column(name = "reserved_at", nullable = false)
    private LocalDateTime reservedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_region", length = 20)
    private Region preferredRegion;

    @Column(name = "total_budget", nullable = false)
    private long totalBudget;

    @Column(name = "base_food_budget", nullable = false)
    private long baseFoodBudget;

    @Column(name = "table_count", nullable = false)
    private int tableCount;

    @Column(name = "per_table_food_budget", nullable = false)
    private long perTableFoodBudget;

    @Column(length = 500)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PartyRequestStatus status;

    private PartyRequest(
            Member student,
            String groupName,
            String purpose,
            int headcount,
            LocalDateTime reservedAt,
            Region preferredRegion,
            long totalBudget,
            long baseFoodBudget,
            String note
    ) {
        if (student == null || !student.isStudent()) {
            throw new IllegalArgumentException("학생 회원만 리퀘스트를 등록할 수 있습니다.");
        }
        if (headcount < 10 || headcount > 300) {
            throw new IllegalArgumentException("인원은 10명 이상 300명 이하여야 합니다.");
        }
        if (reservedAt == null || !reservedAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("예약 일시는 현재 이후여야 합니다.");
        }
        if (totalBudget < 10_000) {
            throw new IllegalArgumentException("총예산은 10,000원 이상이어야 합니다.");
        }
        if (baseFoodBudget <= 0 || baseFoodBudget > totalBudget) {
            throw new IllegalArgumentException("기본 안주 예산은 0원보다 크고 총예산 이하여야 합니다.");
        }
        this.student = student;
        this.groupName = requireText(groupName, "단체명");
        this.purpose = requireText(purpose, "단체 목적");
        this.headcount = headcount;
        this.reservedAt = reservedAt;
        this.preferredRegion = preferredRegion;
        this.totalBudget = totalBudget;
        this.baseFoodBudget = baseFoodBudget;
        this.tableCount = (headcount + PEOPLE_PER_TABLE - 1) / PEOPLE_PER_TABLE;
        this.perTableFoodBudget = baseFoodBudget / tableCount;
        this.note = note;
        this.status = PartyRequestStatus.OPEN;
    }

    public static PartyRequest open(
            Member student,
            String groupName,
            String purpose,
            int headcount,
            LocalDateTime reservedAt,
            Region preferredRegion,
            long totalBudget,
            long baseFoodBudget,
            String note
    ) {
        return new PartyRequest(
                student,
                groupName,
                purpose,
                headcount,
                reservedAt,
                preferredRegion,
                totalBudget,
                baseFoodBudget,
                note
        );
    }

    public boolean isOpenAt(LocalDateTime now) {
        return status == PartyRequestStatus.OPEN && reservedAt.isAfter(now);
    }

    public void match(LocalDateTime now) {
        if (!isOpenAt(now)) {
            throw new IllegalStateException("모집 중인 리퀘스트만 체결할 수 있습니다.");
        }
        this.status = PartyRequestStatus.MATCHED;
    }

    public void close() {
        if (status != PartyRequestStatus.OPEN) {
            throw new IllegalStateException("모집 중인 리퀘스트만 마감할 수 있습니다.");
        }
        this.status = PartyRequestStatus.CLOSED;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + "은(는) 비어 있을 수 없습니다.");
        }
        return value;
    }
}
