package com.fa.ims.service.impl;

import com.fa.ims.entity.Job;
import com.fa.ims.entity.JobSkill;
import com.fa.ims.entity.Skill;
import com.fa.ims.enums.JobStatus;
import com.fa.ims.repository.JobRepository;
import com.fa.ims.service.JobService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;

@Service
public class JobServiceImpl extends BaseServiceImpl<Job, Long, JobRepository> implements JobService {
    @Override
    public Job createNew(Job entity) {
        entity.setJobStatus(JobStatus.OPEN);
        return super.createNew(entity);
    }

    @Override
    public Specification<Job> buildSpecForSearch(String searchKey) {
        if (!StringUtils.hasLength(searchKey)) {
            throw new IllegalArgumentException("Can not build spec for null key");
        }
        return (root, query, criteriaBuilder) -> {
            Join<Job, JobSkill> jobSkillJoin = root.join("requiredSkillSet", JoinType.LEFT);
            Join<JobSkill, Skill> skillJoin = jobSkillJoin.join("skill", JoinType.LEFT);
            query.distinct(true);
            query.select(skillJoin.get("skillName")).where(criteriaBuilder.isNotNull(skillJoin.get("skillName")));
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jobTitle")), searchLikeLowercaseKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jobLevel")), searchLikeLowercaseKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(skillJoin.get("skillName")), searchLikeLowercaseKeyword(searchKey))
            );
        };
    }
}
