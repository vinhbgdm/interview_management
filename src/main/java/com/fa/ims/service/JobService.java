package com.fa.ims.service;

import com.fa.ims.entity.Job;
import org.springframework.data.jpa.domain.Specification;

public interface JobService extends BaseService<Job, Long> {

    Specification<Job> buildSpecForSearch(String searchKey);
}