package com.fa.ims.service;

import com.fa.ims.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenService extends BaseService <PasswordResetToken, Long > {
    Optional<PasswordResetToken> findByDeletedFalseAndToken (String token);
}
