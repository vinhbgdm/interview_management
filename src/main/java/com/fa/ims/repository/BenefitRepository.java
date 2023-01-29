package com.fa.ims.repository;

import com.fa.ims.entity.Benefit;
import com.fa.ims.entity.Skill;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BenefitRepository extends BaseRepository<Benefit, Long>{
    Optional<Benefit> findByBenefitAndDeletedFalse(String name);

}
