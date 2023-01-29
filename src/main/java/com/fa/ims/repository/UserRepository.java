package com.fa.ims.repository;

import com.fa.ims.dto.LastUsernameDto;
import com.fa.ims.entity.User;
import com.fa.ims.enums.UserRole;
import com.fa.ims.enums.UserStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByPhoneAndDeletedFalse(String phone);

    Optional<User> findByUsernameAndDeletedFalse(String username);

    Optional<User> findByUsernameAndDeletedFalseAndUserStatus(String username, UserStatus userStatus);

    User findByUsername(String username);

    List<User> findAllByDeletedFalseAndUserStatusAndUserRoleIn(UserStatus userStatus, List<UserRole> userRoleList);

    @Query(name = "findLastUsername", nativeQuery = true)
    LastUsernameDto getLastUsername(String username);

    Optional<User> findByDeletedFalseAndEmail(String email);

    boolean existsByDeletedFalseAndEmail(String email);
}
