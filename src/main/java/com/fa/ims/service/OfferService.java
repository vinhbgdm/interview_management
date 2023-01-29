package com.fa.ims.service;

import com.fa.ims.dto.OfferReminderDto;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.Job;
import com.fa.ims.entity.Offer;
import com.fa.ims.enums.CandidateStatus;
import com.fa.ims.enums.OfferStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OfferService extends BaseService<Offer, Long>{

    Specification<Offer> buildSpecForSearch(String searchKey);

    List<Offer> findAllByDateBetween(LocalDate fromDate, LocalDate toDate);

    void deleteLogicByCandidate_Id(Long candidateId);

    Offer cancel(Offer entity);

    boolean existsCandidateInAnotherOffer(Long candidateId);

    List<Offer> findAllOfferWithWaitingForApprovalStatus ();
    Specification<Offer> filterRecruiterView(Long id);

    Specification<Offer> filterApproveView(Long id);

    List<OfferReminderDto> getOfferReminder();

    OfferReminderDto getTotalOfferNeedApproveById(Long id);


}
