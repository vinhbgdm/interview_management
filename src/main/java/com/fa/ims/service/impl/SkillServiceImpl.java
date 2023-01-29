package com.fa.ims.service.impl;

import com.fa.ims.entity.Skill;
import com.fa.ims.repository.SkillRepository;
import com.fa.ims.service.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor

public class SkillServiceImpl
        extends BaseServiceImpl<Skill, Long, SkillRepository>
        implements SkillService {


    @Override
    public Optional<Skill> findBySkillName(String name) {
        return repository.findBySkillNameAndDeletedFalse(name);
    }
}
