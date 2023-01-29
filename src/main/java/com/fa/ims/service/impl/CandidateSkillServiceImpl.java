package com.fa.ims.service.impl;

import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.CandidateSkill;
import com.fa.ims.repository.CandidateSkillRepository;
import com.fa.ims.service.BaseService;
import com.fa.ims.service.CandidateSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateSkillServiceImpl extends BaseServiceImpl<CandidateSkill, Long, CandidateSkillRepository> implements CandidateSkillService {



    @Override
    public void deleteByCandidateId(Long id) {
        repository.deleteAllByCandidate_Id(id);
    }
}
