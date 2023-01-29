package com.fa.ims.service.impl;

import com.fa.ims.entity.*;
import com.fa.ims.enums.CandidateStatus;
import com.fa.ims.enums.InterviewResult;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.enums.InterviewScheduleStatus;
import com.fa.ims.repository.InterviewScheduleRepository;
import com.fa.ims.service.InterviewScheduleService;
import com.fa.ims.util.EnumUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.List;

@Service
public class InterviewScheduleServiceImpl extends BaseServiceImpl<InterviewSchedule, Long, InterviewScheduleRepository>
        implements InterviewScheduleService {

    @Override
    public Specification<InterviewSchedule> buildSpecForSearch(String searchKey) {
        if (!StringUtils.hasLength(searchKey)) {
            throw new IllegalArgumentException("Can not build spec for null key");
        }

        return  (root, query, criteriaBuilder) -> {
            Join<InterviewSchedule, InterviewScheduleUser> join01 = root.join("interviewerSet", JoinType.LEFT);
            Join<InterviewScheduleUser,User> interviewerJoin = join01.join("user",JoinType.LEFT);
            Join<InterviewSchedule, Candidate> interviewScheduleCandidateJoin = root.join("candidate", JoinType.LEFT);
            query.distinct(true);
            Predicate searchInterviewPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchLikeLowercaseKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(interviewerJoin.get("username")), searchLikeLowercaseKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(interviewScheduleCandidateJoin.get("fullName")),
                            searchLikeLowercaseKeyword(searchKey))
            );

            List<InterviewResult> interviewResults = EnumUtils
                    .getEnumByNameContain(InterviewResult.class, searchKey);
            if (!CollectionUtils.isEmpty(interviewResults)) {
                searchInterviewPredicate = criteriaBuilder.or(searchInterviewPredicate, root.get("interviewResult").in(interviewResults));
            }

            return searchInterviewPredicate;
        };
    }

    @Override
    public InterviewSchedule createNew(InterviewSchedule entity) {
        entity.setDeleted(false);
        entity.setInterviewStatus(InterviewScheduleStatus.OPEN);
        entity.getCandidate().setCandidateStatus(CandidateStatus.WAITING_FOR_INTERVIEW);
        return super.createNew(entity);
    }

    @Override
    public List<InterviewSchedule> findByCandidateId(Long candidateId) {
        return repository.findByDeletedFalseAndInterviewResultAndCandidate_Id(InterviewResult.PASS, candidateId );
    }

    @Override
    public void deleteLogicByCandidate_Id(Long candidateId) {
        repository.deleteLogicByCandidate_Id(candidateId);
    }

}
