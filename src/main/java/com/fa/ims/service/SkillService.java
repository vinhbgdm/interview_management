package com.fa.ims.service;

import com.fa.ims.entity.Skill;

import java.util.Optional;

public interface SkillService extends BaseService<Skill, Long>{
    Optional<Skill> findBySkillName(String name);

}
