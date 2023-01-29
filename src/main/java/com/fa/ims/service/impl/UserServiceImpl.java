package com.fa.ims.service.impl;

import com.fa.ims.dto.LastUsernameDto;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.Offer;
import com.fa.ims.entity.Skill;
import com.fa.ims.entity.User;
import com.fa.ims.enums.UserRole;
import com.fa.ims.enums.UserStatus;
import com.fa.ims.repository.SkillRepository;
import com.fa.ims.repository.UserRepository;
import com.fa.ims.security.SecurityUtil;
import com.fa.ims.service.SkillService;
import com.fa.ims.service.UserService;
import com.fa.ims.util.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserRepository>
        implements UserService {

    @Override
    public List<User> findAllRecruiter() {
        return repository.findAllByDeletedFalseAndUserStatusAndUserRoleIn(UserStatus.ACTIVE,
                Arrays.asList(UserRole.ROLE_ADMIN, UserRole.ROLE_MANAGER, UserRole.ROLE_RECRUITER));
    }

    @Override
    public List<User> findAllManager() {
        return repository.findAllByDeletedFalseAndUserStatusAndUserRoleIn(UserStatus.ACTIVE,
                Arrays.asList(UserRole.ROLE_ADMIN, UserRole.ROLE_MANAGER));
    }

    @Override
    public List<User> findAllInterviewer() {
        return repository.findAllByDeletedFalseAndUserStatusAndUserRoleIn(UserStatus.ACTIVE,
                List.of(UserRole.ROLE_INTERVIEWER));
    }

    @Override
    public Long findIdByUsername(String username) {
        Optional<User> userOptional = repository.findByUsernameAndDeletedFalseAndUserStatus(username, UserStatus.ACTIVE);
        return userOptional.map(User::getId).orElse(null);
    }

    @Override
    public Optional<User> findByDeletedFalseAndEmail(String email) {
        return repository.findByDeletedFalseAndEmail(email);
    }


    @Override
    public boolean existsByDeletedFalseAndEmail(String email) {
        return repository.existsByDeletedFalseAndEmail(email);
    }

    @Transactional()
    public void updateNewPassword(User user, String newPassword) {
        user.setPassword(SecurityUtil.encryptPasswordByBcryptEncoder(newPassword));
        update(user);
    }
    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsernameAndDeletedFalseAndUserStatus(username, UserStatus.ACTIVE);
    }

    public Specification<User> buildSpecForSearch(String searchKey) {

        if (!StringUtils.hasLength(searchKey)) {
            throw new IllegalArgumentException("Can not build spec for null key");
        }
        return (root, query, criteriaBuilder) -> {
            Predicate searchUser = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), searchLikeLowercaseKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchLikeLowercaseKeyword(searchKey)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), searchLikeLowercaseKeyword(searchKey))
            );
            List<UserStatus> userStatus = EnumUtils
                    .getEnumByNameContain(UserStatus.class, searchKey);
            if (!CollectionUtils.isEmpty(userStatus)) {
                searchUser = criteriaBuilder.or(searchUser, root.get("userStatus").in(userStatus));
            }
            return searchUser;
        };
    }

    @Override
    public LastUsernameDto getLastUsername(String username) {
        return repository.getLastUsername(username + "%");
    }

    @Override
	public boolean existEmail(String email) {
		return repository.existsByEmailAndDeletedFalse(email);
	}
	@Override
	public boolean existPhone(String phone) {
		return repository.existsByPhoneAndDeletedFalse(phone);
	}
}
