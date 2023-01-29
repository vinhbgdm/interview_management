package com.fa.ims.service.impl;

import com.fa.ims.entity.JobBenefit;
import com.fa.ims.repository.JobBenefitRepository;
import com.fa.ims.service.JobBenefitService;
import org.springframework.stereotype.Service;

@Service
public class JobBenefitServiceImpl extends BaseServiceImpl<JobBenefit, Long, JobBenefitRepository>
        implements JobBenefitService {
}
