package com.fa.ims.repository;

import com.fa.ims.entity.Candidate;

import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.User;
import com.fa.ims.enums.CandidateStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;


public interface CandidateRepository extends BaseRepository<Candidate, Long> {

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByPhoneAndDeletedFalse(String phone);


    List<Candidate> findAllByDeletedFalseAndCandidateStatusIn(List<CandidateStatus> candidateStatusList);
    List<Candidate> findAllByDeletedFalseAndCandidateStatusInOrId(List<CandidateStatus> candidateStatusList, Long id);
    boolean existsByDobAndDeletedFalse(LocalDate localDate);

    @Transactional
    @Modifying
    @Query(value = "update Candidate c set c.attachFile=null where c.attachFile=:attachFile")
    void updateNullAttachFile(String attachFile);

    List<Candidate> findByDeletedFalseAndCandidateStatusIn(List<CandidateStatus> candidateStatusList);

    @Query(value = "select c.recruiter from Candidate c where c.deleted = false and c.id=:candidateId")
    User findRecruiterByDeletedFalseAndCandidateId(Long candidateId);
}
