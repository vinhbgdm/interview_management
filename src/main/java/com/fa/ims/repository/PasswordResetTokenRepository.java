package com.fa.ims.repository;

import com.fa.ims.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken, Long > {

    Optional<PasswordResetToken> findByDeletedFalseAndToken (String token);
}
