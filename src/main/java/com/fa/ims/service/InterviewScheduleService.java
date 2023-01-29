package com.fa.ims.service;

import com.fa.ims.entity.InterviewSchedule;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface InterviewScheduleService extends BaseService<InterviewSchedule, Long> {
    Specification<InterviewSchedule> buildSpecForSearch(String searchKey);

    List<InterviewSchedule> findByCandidateId(Long candidateId);

    void deleteLogicByCandidate_Id(Long candidateId);
}
