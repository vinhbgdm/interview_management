package com.fa.ims.service.impl;

import com.fa.ims.entity.Benefit;
import com.fa.ims.repository.BenefitRepository;
import com.fa.ims.service.BenefitService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor

public class BenefitServiceImpl
        extends BaseServiceImpl<Benefit, Long, BenefitRepository>
        implements BenefitService {



    @Override
    public Optional<Benefit> findByBenefit(String name) {
        return repository.findByBenefitAndDeletedFalse(name);
    }
}
