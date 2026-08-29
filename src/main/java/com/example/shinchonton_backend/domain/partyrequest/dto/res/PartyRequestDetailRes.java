package com.example.shinchonton_backend.domain.partyrequest.dto.res;

import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequestStatus;
import com.example.shinchonton_backend.domain.store.entity.Region;

import java.time.LocalDateTime;

public record PartyRequestDetailRes(
        Long id,
        Long studentId,
        String groupName,
        String purpose,
        int headcount,
        LocalDateTime reservedAt,
        Region preferredRegion,
        long totalBudget,
        long baseFoodBudget,
        int tableCount,
        long perTableFoodBudget,
        String note,
        PartyRequestStatus status,
        LocalDateTime createdAt
) {

    public static PartyRequestDetailRes from(PartyRequest partyRequest) {
        return new PartyRequestDetailRes(
                partyRequest.getId(),
                partyRequest.getStudent().getId(),
                partyRequest.getGroupName(),
                partyRequest.getPurpose(),
                partyRequest.getHeadcount(),
                partyRequest.getReservedAt(),
                partyRequest.getPreferredRegion(),
                partyRequest.getTotalBudget(),
                partyRequest.getBaseFoodBudget(),
                partyRequest.getTableCount(),
                partyRequest.getPerTableFoodBudget(),
                partyRequest.getNote(),
                partyRequest.getStatus(),
                partyRequest.getCreatedAt()
        );
    }
}
