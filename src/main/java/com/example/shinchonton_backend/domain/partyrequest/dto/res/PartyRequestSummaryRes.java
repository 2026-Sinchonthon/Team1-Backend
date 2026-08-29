package com.example.shinchonton_backend.domain.partyrequest.dto.res;

import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequestStatus;
import com.example.shinchonton_backend.domain.store.entity.Region;

import java.time.LocalDateTime;

public record PartyRequestSummaryRes(
        Long id,
        String groupName,
        String purpose,
        int headcount,
        LocalDateTime reservedAt,
        Region preferredRegion,
        long totalBudget,
        int tableCount,
        long perTableFoodBudget,
        PartyRequestStatus status,
        LocalDateTime createdAt
) {

    public static PartyRequestSummaryRes from(PartyRequest partyRequest) {
        return new PartyRequestSummaryRes(
                partyRequest.getId(),
                partyRequest.getGroupName(),
                partyRequest.getPurpose(),
                partyRequest.getHeadcount(),
                partyRequest.getReservedAt(),
                partyRequest.getPreferredRegion(),
                partyRequest.getTotalBudget(),
                partyRequest.getTableCount(),
                partyRequest.getPerTableFoodBudget(),
                partyRequest.getStatus(),
                partyRequest.getCreatedAt()
        );
    }
}
