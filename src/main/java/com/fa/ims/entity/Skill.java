package com.fa.ims.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Skill extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_name")
    private String skillName;

    @OneToMany(mappedBy = "skill", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<JobSkill> jobSkillSet;

    @OneToMany(mappedBy = "candidate", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<CandidateSkill> candidateSkillSet;
}
