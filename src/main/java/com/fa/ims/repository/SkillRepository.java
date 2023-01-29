package com.fa.ims.repository;

import com.fa.ims.entity.Skill;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends BaseRepository<Skill, Long>{
    Optional<Skill> findBySkillNameAndDeletedFalse(String name);
}