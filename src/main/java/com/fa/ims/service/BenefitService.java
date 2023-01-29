package com.fa.ims.service;

import com.fa.ims.entity.Benefit;

import java.util.Optional;

public interface BenefitService extends BaseService<Benefit, Long>{
    Optional<Benefit> findByBenefit(String name);

}
