package com.fa.ims.service;

import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.InterviewScheduleUser;
import com.fa.ims.entity.User;

import java.util.List;

public interface InterviewScheduleUserService extends BaseService<InterviewScheduleUser, Long>{
    List<User> findUserByInterviewScheduleId(Long interviewId);

}
