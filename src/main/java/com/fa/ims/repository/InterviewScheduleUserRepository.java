package com.fa.ims.repository;

import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.InterviewScheduleUser;
import com.fa.ims.entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterviewScheduleUserRepository extends BaseRepository<InterviewScheduleUser, Long> {
    @Query(value = "SELECT isu.user from InterviewScheduleUser isu WHERE isu.interviewerSet.id = :interviewId AND isu.deleted = false ")
    List<User> findUserByInterviewScheduleIdAndDeletedFalse(Long interviewId);
}
