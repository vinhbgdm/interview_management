package com.fa.ims.controller.rest;

import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.Job;
import com.fa.ims.enums.InterviewScheduleStatus;
import com.fa.ims.enums.JobStatus;
import com.fa.ims.service.JobService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/jobs")

public class JobResource {

    private final JobService jobService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Job> jobOptional = jobService.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            //check Interview status
            System.out.println("saaaa");
            job.getInterviewSchedule().forEach(s-> System.out.println("sdf "+s.getId()));
            if (job.getInterviewSchedule().isEmpty()) {
                jobService.delete(job);
                return ResponseEntity.noContent().build();
            } else ResponseEntity.notFound().build();

        }
        return ResponseEntity.notFound().build();
    }
}