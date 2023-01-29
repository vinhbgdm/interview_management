package com.fa.ims.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class CandidateSkill extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    public CandidateSkill(Candidate candidate, Skill skill) {
        this.skill = skill;
        this.candidate = candidate;
    }
}
