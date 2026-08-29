package com.example.shinchonton_backend.domain.partyrequest.repository;

import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequestStatus;
import com.example.shinchonton_backend.domain.store.entity.Region;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PartyRequestRepository extends JpaRepository<PartyRequest, Long> {

    List<PartyRequest> findAllByStudent_IdOrderByCreatedAtDesc(Long studentId);

    @Query("""
            select request
            from PartyRequest request
            where request.status = :status
              and request.reservedAt > :now
              and request.headcount <= :maxCapacity
              and (:region is null
                   or request.preferredRegion is null
                   or request.preferredRegion = :region)
            order by request.createdAt desc
            """)
    List<PartyRequest> findAllAvailableForStore(
            @Param("status") PartyRequestStatus status,
            @Param("now") LocalDateTime now,
            @Param("maxCapacity") int maxCapacity,
            @Param("region") Region region
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select request from PartyRequest request where request.id = :id")
    Optional<PartyRequest> findByIdForUpdate(@Param("id") Long id);
}
