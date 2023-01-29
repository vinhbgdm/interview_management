package com.fa.ims.service.impl;

import com.fa.ims.dto.OfferReminderDto;
import com.fa.ims.entity.*;

import com.fa.ims.enums.*;
import com.fa.ims.repository.OfferRepository;
import com.fa.ims.service.OfferService;
import com.fa.ims.util.EnumUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class OfferServiceImpl extends BaseServiceImpl<Offer, Long, OfferRepository>
        implements OfferService {

    @Override
    public Offer createNew(Offer entity) {
        entity.getCandidate().setCandidateStatus(CandidateStatus.WAITING_FOR_APPROVAL);
        return super.createNew(entity);
    }

    @Override
    public Offer update(Offer entity) {
        entity.getCandidate().setCandidateStatus(CandidateStatus.valueOf(entity.getOfferStatus().toString().toUpperCase()));
        return super.update(entity);
    }

    @Override
    public Offer cancel(Offer entity) {
        entity.setOfferStatus(OfferStatus.CANCELLED_OFFER);
        entity.getCandidate().setCandidateStatus(CandidateStatus.CANCELLED_OFFER);
        return super.update(entity);
    }

    @Override
    public boolean existsCandidateInAnotherOffer(Long candidateId) {
        return repository.existsByCandidate_IdAndOfferStatusIn(candidateId,
                Arrays.asList(OfferStatus.WAITING_FOR_APPROVAL, OfferStatus.APPROVED_OFFER));
    }


    @Override
    public Specification<Offer> buildSpecForSearch(String searchKey) {
        if (!StringUtils.hasLength(searchKey)) {
            throw new IllegalArgumentException("Can not build spec for null key");
        }
        return  (root, query, criteriaBuilder) -> {
            Join<Offer, Candidate> offerCandidateJoin = root.join("candidate", JoinType.INNER);
            Join<Offer, User> offerUserJoin = root.join("approvedBy", JoinType.INNER);
            Predicate searchOffer = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("note")), searchLikeLowercaseKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(offerCandidateJoin.get("fullName")), searchLikeLowercaseKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(offerCandidateJoin.get("email")), searchLikeLowercaseKeyword(searchKey))
            );
            List<UserRole> userRoles = EnumUtils
                    .getEnumByNameContain(UserRole.class, searchKey);
            if (!CollectionUtils.isEmpty(userRoles)) {
                searchOffer = criteriaBuilder.or(searchOffer, offerUserJoin.get("userRole").in(userRoles));
            }
            return searchOffer;
        };
    }

    @Override
    public List<Offer> findAllByDateBetween(LocalDate fromDate, LocalDate toDate) {
        return repository.findAllByDeletedFalseAndCreatedDateBetween(fromDate.atTime(0,0),
                toDate.atTime(0,0) );
    }

    @Override
    public void deleteLogicByCandidate_Id(Long candidateId) {
        repository.deleteLogicByCandidate_Id(candidateId);
    }

    @Override
    public List<Offer> findAllOfferWithWaitingForApprovalStatus() {
        return repository.findAllByDeletedFalseAndOfferStatus(OfferStatus.WAITING_FOR_APPROVAL);
    }

    @Override
    public Specification<Offer> filterRecruiterView(Long id) {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.equal(root.get("recruiterOwner"), id);
        });
    }

    @Override
    public Specification<Offer> filterApproveView(Long id) {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Offer, User> offerUserJoin = root.join("approvedBy", JoinType.INNER);
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("offerStatus"), OfferStatus.WAITING_FOR_APPROVAL),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), LocalDate.now()),
                    criteriaBuilder.equal(offerUserJoin.get("id"), id)
            );
        });
    }


    @Override
    public List<OfferReminderDto> getOfferReminder() {
        return repository.getOfferReminder(LocalDate.now());
    }

    @Override
    public OfferReminderDto getTotalOfferNeedApproveById(Long id) {
        return repository.getTotalOfferNeedApproveById(LocalDate.now(), id);
    }

}
