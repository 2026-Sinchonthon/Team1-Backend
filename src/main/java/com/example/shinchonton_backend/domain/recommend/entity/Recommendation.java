package com.example.shinchonton_backend.domain.recommend.entity;

import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "recommendations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_recommendation_request_store_budget",
                columnNames = {"party_request_id", "store_id", "table_budget"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 학생 리퀘스트에서 생성된 추천인지 저장한다.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_request_id", nullable = false)
    private PartyRequest partyRequest;

    // 어떤 가게의 메뉴판을 기준으로 추천했는지 저장한다.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "table_budget", nullable = false)
    private long tableBudget;

    @Column(name = "table_count", nullable = false)
    private int tableCount;

    @Column(nullable = false)
    private int headcount;

    // 추천 결과는 화면에 다시 보여주기 쉽도록 JSON 원문 그대로 저장한다.
    @Lob
    @Column(name = "result_json", nullable = false, columnDefinition = "TEXT")
    private String resultJson;

    private Recommendation(
            PartyRequest partyRequest,
            Store store,
            long tableBudget,
            int tableCount,
            int headcount,
            String resultJson
    ) {
        this.partyRequest = partyRequest;
        this.store = store;
        this.tableBudget = tableBudget;
        this.tableCount = tableCount;
        this.headcount = headcount;
        this.resultJson = resultJson;
    }

    public static Recommendation create(
            PartyRequest partyRequest,
            Store store,
            long tableBudget,
            String resultJson
    ) {
        return new Recommendation(
                partyRequest,
                store,
                tableBudget,
                partyRequest.getTableCount(),
                partyRequest.getHeadcount(),
                resultJson
        );
    }
}
