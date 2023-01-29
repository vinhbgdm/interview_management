package com.fa.ims.service.impl;

import com.fa.ims.entity.PasswordResetToken;
import com.fa.ims.repository.PasswordResetTokenRepository;
import com.fa.ims.service.PasswordResetTokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetTokenServiceImpl extends BaseServiceImpl <PasswordResetToken, Long, PasswordResetTokenRepository>
        implements PasswordResetTokenService {


    @Override
    public Optional<PasswordResetToken> findByDeletedFalseAndToken(String token) {
        return repository.findByDeletedFalseAndToken(token);
    }
}
