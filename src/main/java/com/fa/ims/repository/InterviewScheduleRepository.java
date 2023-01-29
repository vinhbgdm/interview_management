package com.fa.ims.repository;

import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.enums.InterviewResult;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewScheduleRepository extends BaseRepository<InterviewSchedule, Long> {
//    @Query(value = "select i from InterviewSchedule i " +
//            "left join Offer o on i.id =o.interviewSchedule.id " +
//            "where o.interviewSchedule.id is null and i.candidate.id=:id")
    List<InterviewSchedule> findByDeletedFalseAndInterviewResultAndCandidate_Id(InterviewResult pass, Long id);

    @Transactional
    @Modifying
    @Query(value = "update InterviewSchedule e set e.deleted = true where e.candidate.id=:candidateId")
    void deleteLogicByCandidate_Id(Long candidateId);

}
