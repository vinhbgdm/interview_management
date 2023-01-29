package com.fa.ims.service;

import com.fa.ims.dto.LastUsernameDto;
import com.fa.ims.entity.Offer;
import com.fa.ims.entity.User;
import com.fa.ims.enums.UserRole;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<User, Long>{
//    List<User> findAllRecruiter();
	
	boolean existEmail(String email);
    boolean existPhone(String phone);
    
    List<User> findAllRecruiter();
    List<User> findAllManager();
    List<User> findAllInterviewer();
    Long findIdByUsername(String username);
    Optional<User> findByDeletedFalseAndEmail (String email);
    boolean existsByDeletedFalseAndEmail (String email);
    Optional<User> findByUsername(String username);
    Specification<User> buildSpecForSearch(String searchKey);

    LastUsernameDto getLastUsername(String username);

}
