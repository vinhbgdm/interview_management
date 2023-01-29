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

public class Benefit extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String benefit;

    @OneToMany(mappedBy = "benefit", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<JobBenefit> jobBenefitSet;
}
