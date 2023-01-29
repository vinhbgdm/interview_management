package com.fa.ims.service.impl;

import com.fa.ims.entity.InterviewScheduleUser;
import com.fa.ims.entity.User;
import com.fa.ims.repository.InterviewScheduleUserRepository;
import com.fa.ims.service.InterviewScheduleUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewScheduleUserServiceImpl extends BaseServiceImpl<InterviewScheduleUser, Long, InterviewScheduleUserRepository>
        implements InterviewScheduleUserService {
    @Override
    public List<User> findUserByInterviewScheduleId(Long interviewId) {
        return repository.findUserByInterviewScheduleIdAndDeletedFalse(interviewId);
    }
}
