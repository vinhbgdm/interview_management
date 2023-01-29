package com.fa.ims.service;

import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.InterviewScheduleUser;
import org.springframework.data.jpa.domain.Specification;

import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.User;
import com.fa.ims.enums.CandidateStatus;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

import java.time.LocalDate;

public interface CandidateService extends BaseService<Candidate, Long> {
    boolean existEmail(String email);

    boolean existPhone(String phone);

    /**
     * tìm candidate đủ điều kiện để tạo offer
     * Status: OPEN và pass Interview
     */
    List<Candidate> findAllCandidateToCreateOffer();

    List<Candidate> findAllCandidateToUpdateOffer(Long id);


    /**
     * this method using to set candidate_attachFile = null after API delete attachfile called
     */
    void updateNullAttachFile(String attachFile);

    User findRecruiterByDeletedFalseAndCandidateId(Long recruiterId);

    Specification<Candidate> buildSpecForSearch(String searchKey);
    Specification<Candidate> filterInterviewerView(Long id);


}
