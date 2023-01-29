package com.fa.ims.service.impl;

import com.fa.ims.entity.JobSkill;
import com.fa.ims.repository.JobSkillRepository;
import com.fa.ims.service.JobSkillService;
import org.springframework.stereotype.Service;

@Service
public class JobSkillServiceImpl extends BaseServiceImpl<JobSkill, Long, JobSkillRepository>
        implements JobSkillService {
}
