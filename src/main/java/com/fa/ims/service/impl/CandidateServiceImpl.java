package com.fa.ims.service.impl;

import com.fa.ims.entity.*;
import com.fa.ims.enums.CandidatePosition;
import com.fa.ims.enums.CandidateStatus;
import com.fa.ims.enums.Department;
import com.fa.ims.repository.CandidateRepository;
import com.fa.ims.service.CandidateService;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.fa.ims.util.EnumUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class CandidateServiceImpl extends BaseServiceImpl<Candidate, Long, CandidateRepository>
        implements CandidateService {

    @Override
    public boolean existEmail(String email) {
        return repository.existsByEmailAndDeletedFalse(email);
    }

    @Override
    public boolean existPhone(String phone) {
        return repository.existsByPhoneAndDeletedFalse(phone);
    }

    @Override
    public List<Candidate> findAllCandidateToCreateOffer() {
        return repository.findAllByDeletedFalseAndCandidateStatusIn(Arrays.asList(CandidateStatus.OPEN,
                CandidateStatus.PASSED_INTERVIEW, CandidateStatus.REJECTED_OFFER));
    }

    @Override
    public List<Candidate> findAllCandidateToUpdateOffer(Long id) {
        return repository.findAllByDeletedFalseAndCandidateStatusInOrId(Arrays.asList(CandidateStatus.OPEN,
                CandidateStatus.PASSED_INTERVIEW, CandidateStatus.REJECTED_OFFER), id);
    }

    @Override
    public void updateNullAttachFile(String attachFile) {
        repository.updateNullAttachFile(attachFile);
    }

    @Override
    public void delete(Candidate entity) {
        entity.setDeleted(true);
        entity.setAttachFile(null);
        super.delete(entity);
    }

    @Override
    public User findRecruiterByDeletedFalseAndCandidateId(Long recruiterId) {
        return repository.findRecruiterByDeletedFalseAndCandidateId(recruiterId);
    }

    @Override
    public Specification<Candidate> buildSpecForSearch(String searchKey) {
        if (!StringUtils.hasLength(searchKey)) {
            throw new IllegalArgumentException("Can not build spec for null key");
        }
        return (root, query, criteriaBuilder) ->
        {

            Join<Candidate, User> candidateUserJoin = root.join("recruiter", JoinType.INNER);
            //			Join<Candidate, CandidateSkill> candidateSkillJoin = root.join("candidateSkillSet", JoinType.LEFT);
            //			Join<CandidateSkill, Skill> skillJoin = candidateSkillJoin.join("skill", JoinType.LEFT);
            query.distinct(true);
            Predicate searchCandidate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(candidateUserJoin.get("username")), searchLikeKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), searchLikeKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchLikeKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), searchLikeKeyword(searchKey))
                    //				criteriaBuilder.like(criteriaBuilder.lower(skillJoin.get("skillName")), searchLikeLowercaseKeyword(searchKey))
            );
            List<CandidatePosition> candidatePosition = EnumUtils
                    .getEnumByNameContain(CandidatePosition.class, searchKey);
            if (!CollectionUtils.isEmpty(candidatePosition)) {
                searchCandidate = criteriaBuilder.or(searchCandidate, root.get("candidatePosition").in(candidatePosition));
            }
            return searchCandidate;
        };
    }

    @Override
    public Specification<Candidate> filterInterviewerView(Long id) {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);

            Join<Candidate, InterviewSchedule> candidateInterviewScheduleJoin
                    = root.join("interviewScheduleSet", JoinType.INNER);

            Join<InterviewSchedule, InterviewScheduleUser> interviewScheduleInterviewScheduleUserJoin
                    = candidateInterviewScheduleJoin.join("interviewerSet", JoinType.INNER);

            //                Join<InterviewScheduleUser, User> interviewScheduleUserUserJoin
            //                        = interviewScheduleInterviewScheduleUserJoin.join("interviewerSet", JoinType.INNER);

            return criteriaBuilder.equal(interviewScheduleInterviewScheduleUserJoin.get("user"), id);
        });
    }
}
