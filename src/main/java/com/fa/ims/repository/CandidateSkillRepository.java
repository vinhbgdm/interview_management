package com.fa.ims.repository;

import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.CandidateSkill;
import com.fa.ims.service.BaseService;

public interface CandidateSkillRepository extends BaseRepository<CandidateSkill, Long> {

    void deleteAllByCandidate_Id(Long id);

}
