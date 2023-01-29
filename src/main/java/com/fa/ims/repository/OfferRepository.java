package com.fa.ims.repository;

import com.fa.ims.dto.OfferReminderDto;
import com.fa.ims.entity.Offer;
import com.fa.ims.enums.OfferStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OfferRepository extends BaseRepository<Offer, Long>{

    List<Offer> findAllByDeletedFalseAndCreatedDateBetween(LocalDateTime createdDate, LocalDateTime createdDate2);

    @Transactional
    @Modifying
    @Query(value = "update Offer o set o.deleted = true where o.candidate.id=:candidateId")
    void deleteLogicByCandidate_Id(Long candidateId);

    boolean existsByCandidate_IdAndOfferStatusIn(Long candidateId, List<OfferStatus> offerStatuses);

    List<Offer> findAllByDeletedFalseAndOfferStatus(OfferStatus offerStatus);

    @Query(name = "ScheduleReminderOffer", nativeQuery = true)
    List<OfferReminderDto> getOfferReminder(LocalDate today);

    @Query(name = "GetTotalOfferById", nativeQuery = true)
    OfferReminderDto getTotalOfferNeedApproveById(LocalDate today, Long id);

}
