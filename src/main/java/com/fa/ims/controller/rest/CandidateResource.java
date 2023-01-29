package com.fa.ims.controller.rest;

import com.fa.ims.dto.CandidateDisplayFormDto;
import com.fa.ims.dto.InterviewScheduleCreateFormOfferDto;
import com.fa.ims.dto.OfferInterviewerDto;
import com.fa.ims.dto.RecruiterDisplayDto;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.InterviewScheduleUser;
import com.fa.ims.entity.User;
import com.fa.ims.service.CandidateService;
import com.fa.ims.service.CandidateSkillService;
import com.fa.ims.service.InterviewScheduleService;
import com.fa.ims.service.OfferService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidates")
public class CandidateResource {
    private final CandidateService candidateService;
    private final InterviewScheduleService interviewScheduleService;
    private final CandidateSkillService candidateSkillService;
    private final OfferService offerService;
    @Value("${app.file.location.cv}")
    String fileLocation;

    public CandidateResource(CandidateService candidateService, InterviewScheduleService interviewScheduleService,
                             CandidateSkillService candidateSkillService, OfferService offerService) {
        this.candidateService = candidateService;
        this.interviewScheduleService = interviewScheduleService;
        this.candidateSkillService = candidateSkillService;
        this.offerService = offerService;
    }

    @DeleteMapping("/{id}/delete")
    @Transactional
    public ResponseEntity<?> deleteCandidateById(@PathVariable Long id) {
        Optional<Candidate> candidateOptional = candidateService.findById(id);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            try {
                FileUtils.delete(new File(fileLocation + "\\" + candidate.getAttachFile()));
            } catch (IOException ignore) {
            }
            candidateSkillService.deleteByCandidateId(candidate.getId());
            interviewScheduleService.deleteLogicByCandidate_Id(candidate.getId());
            offerService.deleteLogicByCandidate_Id(candidate.getId());
            candidateService.delete(candidate);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * find interview schedule by candidate ID with interview status = pass
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/schedule-interview")
    public ResponseEntity<?> getScheduleInterviewById(@PathVariable Long id) {
        List<InterviewSchedule> interviewScheduleList = interviewScheduleService.findByCandidateId(id); //FIXME : get passed_interview status
        if (!interviewScheduleList.isEmpty()) {

            Set<InterviewScheduleCreateFormOfferDto> interviewScheduleCreateFormOfferDtoList
                    = interviewScheduleList.stream().map(interviewSchedule -> {
                Set<OfferInterviewerDto> interviewScheduleUserSet = interviewSchedule.getInterviewerSet()
                        .stream()
                        .map(setRecruiter -> new OfferInterviewerDto(setRecruiter.getId(), setRecruiter.getUser().getUsername()))
                        .collect(Collectors.toSet());

                return new InterviewScheduleCreateFormOfferDto(interviewSchedule.getId(),
                        interviewSchedule.getTitle(), interviewSchedule.getNote(),
                        interviewSchedule.getCandidate().getId(),
                        interviewScheduleUserSet);
            }).collect(Collectors.toSet());

            return ResponseEntity.status(HttpStatus.OK).body(
                    interviewScheduleCreateFormOfferDtoList);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/recruiter")
    public ResponseEntity<?> getRecruiterOfCandidate(@PathVariable Long id) {
        User recruiter = candidateService.findRecruiterByDeletedFalseAndCandidateId(id);
        if (recruiter != null) {
            RecruiterDisplayDto recruiterDisplayDto =
                    new RecruiterDisplayDto(recruiter.getId(), recruiter.getUsername(), recruiter.getUserRole(), recruiter.getFullName());

            return ResponseEntity.status(HttpStatus.OK).body(
                    recruiterDisplayDto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/position")
    public ResponseEntity<?> getPositionOfCandidate(@PathVariable Long id) {
        Candidate candidate = candidateService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found candidate with id " + id));

        CandidateDisplayFormDto candidateDisplayFormDto = new CandidateDisplayFormDto();

        BeanUtils.copyProperties(candidate, candidateDisplayFormDto);

        return ResponseEntity.status(HttpStatus.OK).body(candidateDisplayFormDto);
    }
}
