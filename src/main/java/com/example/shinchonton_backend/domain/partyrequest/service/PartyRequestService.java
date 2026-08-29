package com.example.shinchonton_backend.domain.partyrequest.service;

import com.example.shinchonton_backend.domain.member.entity.Member;
import com.example.shinchonton_backend.domain.member.repository.MemberRepository;
import com.example.shinchonton_backend.domain.partyrequest.dto.req.PartyRequestCreateReq;
import com.example.shinchonton_backend.domain.partyrequest.dto.res.PartyRequestCreateRes;
import com.example.shinchonton_backend.domain.partyrequest.dto.res.PartyRequestDetailRes;
import com.example.shinchonton_backend.domain.partyrequest.dto.res.PartyRequestSummaryRes;
import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequestStatus;
import com.example.shinchonton_backend.domain.partyrequest.repository.PartyRequestRepository;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.domain.store.repository.StoreRepository;
import com.example.shinchonton_backend.global.apiPayload.code.status.GeneralErrorCode;
import com.example.shinchonton_backend.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyRequestService {

    private final PartyRequestRepository partyRequestRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public PartyRequestCreateRes create(PartyRequestCreateReq request) {
        Member student = findStudent(request.studentId());
        validateBudget(request);

        PartyRequest partyRequest = PartyRequest.open(
                student,
                request.groupName(),
                request.purpose(),
                request.headcount(),
                request.reservedAt(),
                request.preferredRegion(),
                request.totalBudget(),
                request.baseFoodBudget(),
                request.note()
        );

        return PartyRequestCreateRes.from(partyRequestRepository.save(partyRequest));
    }

    @Transactional(readOnly = true)
    public List<PartyRequestSummaryRes> findAvailableForStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        return partyRequestRepository.findAllAvailableForStore(
                        PartyRequestStatus.OPEN,
                        LocalDateTime.now(),
                        store.getMaxCapacity(),
                        store.getRegion()
                ).stream()
                .map(PartyRequestSummaryRes::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PartyRequestSummaryRes> findMine(Long studentId) {
        findStudent(studentId);
        return partyRequestRepository.findByUserIdOrderByIdDesc(studentId).stream()
                .map(PartyRequestSummaryRes::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PartyRequestDetailRes findDetail(Long requestId) {
        PartyRequest partyRequest = partyRequestRepository.findById(requestId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));
        return PartyRequestDetailRes.from(partyRequest);
    }

    private Member findStudent(Long studentId) {
        Member student = memberRepository.findById(studentId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));
        if (!student.isStudent()) {
            throw new GeneralException(GeneralErrorCode.FORBIDDEN);
        }
        return student;
    }

    private void validateBudget(PartyRequestCreateReq request) {
        if (request.baseFoodBudget() > request.totalBudget()) {
            throw new GeneralException(GeneralErrorCode.BAD_REQUEST);
        }
    }
}
