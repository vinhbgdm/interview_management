package com.fa.ims.controller.rest;

import com.fa.ims.entity.User;
import com.fa.ims.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {this.userService = userService;}

    @GetMapping("/{username}/get-department")
    public ResponseEntity<?> getDepartmentByUsername(@PathVariable String username) {
        Optional<User> currentUserLoginOpt = userService.findByUsername(username);
        if (currentUserLoginOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(currentUserLoginOpt.get().getDepartment());
        }
        return ResponseEntity.notFound().build();
    }
}
