package com.fa.ims.service;

import com.fa.ims.entity.User;

import java.util.Optional;

public interface AuthenticationService {
    Optional<User> findByUsername(String username);
}
