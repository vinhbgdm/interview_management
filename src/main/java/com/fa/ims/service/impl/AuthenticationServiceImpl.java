package com.fa.ims.service.impl;

import com.fa.ims.entity.User;
import com.fa.ims.enums.UserStatus;
import com.fa.ims.repository.UserRepository;
import com.fa.ims.service.AuthenticationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl extends BaseServiceImpl<User, Long, UserRepository>
        implements AuthenticationService {

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsernameAndDeletedFalseAndUserStatus(username, UserStatus.ACTIVE);
    }
}
