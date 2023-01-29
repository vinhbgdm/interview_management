package com.fa.ims.service;

import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.CandidateSkill;

public interface CandidateSkillService extends BaseService<CandidateSkill, Long> {

    void deleteByCandidateId(Long id);
}
