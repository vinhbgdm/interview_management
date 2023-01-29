package com.fa.ims.entity;

import com.fa.ims.dto.CandidateDisplayFormDto;
import com.fa.ims.dto.OfferDisplayFormDto;
import com.fa.ims.dto.OfferReminderDto;
import com.fa.ims.enums.CandidatePosition;
import com.fa.ims.enums.Department;
import com.fa.ims.enums.OfferLevel;
import com.fa.ims.enums.OfferStatus;
import com.fa.ims.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//FIXME: thay username = fullName, thêm gender
@NamedNativeQuery(
        name = "ScheduleReminderOffer",
        query = "SELECT count(*) as total, o.approved_by_id as approvedId, u.username as username, u.email as email, u.gender as gender, u.full_name as fullName  " +
                "FROM dev_ims_db.offer as o \n" +
                "inner join dev_ims_db.users as u \n" +
                "on o.approved_by_id = u.id\n" +
                "where o.due_date >= ?1\n" +
                "and o.offer_status = 'WAITING_FOR_APPROVAL'" +
                "group by o.approved_by_id, u.username, u.email, u.gender, u.full_name",
        resultSetMapping = "ScheduleResult"
)
@NamedNativeQuery(
        name = "GetTotalOfferById",
        query = "SELECT count(*) as total, o.approved_by_id as approvedId, u.username as username, u.email as email, u.gender as gender, u.full_name as fullName " +
                "FROM dev_ims_db.offer as o \n" +
                "inner join dev_ims_db.users as u \n" +
                "on o.approved_by_id = u.id\n" +
                "where o.due_date >= ?1\n" +
                "and u.id = ?2\n" +
                "and o.offer_status = 'WAITING_FOR_APPROVAL'" +
                "group by o.approved_by_id, u.username, u.email, u.gender, u.full_name",
        resultSetMapping = "ScheduleResult"
)
@SqlResultSetMapping(
        name = "ScheduleResult",
        classes = {
                @ConstructorResult(
                        targetClass = OfferReminderDto.class,
                        columns = {
                                @ColumnResult(name = "total", type = Long.class),
                                @ColumnResult(name = "approvedId", type = Long.class),
                                @ColumnResult(name = "username", type = String.class),
                                @ColumnResult(name = "email", type = String.class),
                                @ColumnResult(name = "gender", type = String.class),
                                @ColumnResult(name = "fullName", type = String.class)
                        })})
public class Offer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_Id")
    private Candidate candidate;

    @OneToOne
    @JoinColumn(name = "interviewSchedule_Id")
    private InterviewSchedule interviewSchedule;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @Enumerated(EnumType.STRING)
    private CandidatePosition position;

    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus;

    @Enumerated(EnumType.STRING)
    private OfferLevel offerLevel;

    @Column(length = 5000)
    private String note;

    @Enumerated(EnumType.STRING)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "approvedBy_id")
    private User approvedBy;

    @ManyToOne
    @JoinColumn(name = "recruiter_owner_id")
    private User recruiterOwner;

    private LocalDate contractFrom;

    private LocalDate contractTo;

    private LocalDate dueDate;

    private BigDecimal basicSalary;

    public OfferDisplayFormDto convertFormToDisplayDto() {
        OfferDisplayFormDto offerDisplayFormDto = new OfferDisplayFormDto();
        BeanUtils.copyProperties(this, offerDisplayFormDto);
        return offerDisplayFormDto;
    }
}
