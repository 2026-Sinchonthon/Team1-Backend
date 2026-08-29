package com.example.shinchonton_backend.domain.partyrequest.dto.res;

import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;

public record PartyRequestCreateRes(Long requestId) {

    public static PartyRequestCreateRes from(PartyRequest partyRequest) {
        return new PartyRequestCreateRes(partyRequest.getId());
    }
}
