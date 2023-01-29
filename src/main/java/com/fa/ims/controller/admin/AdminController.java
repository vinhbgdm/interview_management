package com.fa.ims.controller.admin;

import com.fa.ims.dto.*;
import com.fa.ims.entity.User;
import com.fa.ims.repository.UserRepository;
import com.fa.ims.service.UserService;
import com.fa.ims.util.VNCharacterUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.fa.ims.controller.BaseController;
import com.fa.ims.enums.UserRole;
import com.fa.ims.util.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fa.ims.service.EmailService;

@Controller
@RequestMapping("/admin")

public class AdminController extends BaseController {
    private final UserService userService;

    private final UserRepository userRepository;
    private final SpringTemplateEngine springTemplateEngine;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, UserRepository userRepository, SpringTemplateEngine springTemplateEngine,
                           EmailService emailService, PasswordEncoder passwordEncoder) {
        super();
        this.userService = userService;
        this.userRepository = userRepository;
        this.springTemplateEngine = springTemplateEngine;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("users/create")
    public String showAddUser(Model model) {
        UserCreateFormDto userCreateFormDto = new UserCreateFormDto();
        model.addAttribute("userCreateFormDto", userCreateFormDto);
        return "user/create";
    }

    @PostMapping("users/create")
    public String addUser(@ModelAttribute("userCreateFormDto")
                          @Valid UserCreateFormDto userCreateFormDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (userService.existEmail(userCreateFormDto.getEmail())) {
            bindingResult.rejectValue("email", "message.0012", "Email is existing");
        }
        if (userService.existPhone(userCreateFormDto.getPhone())) {
            bindingResult.rejectValue("phone", "message.0015", "Phone is existing");
        }
        if (bindingResult.hasErrors()) {
            return "user/create";
        }
        User user = new User();
        BeanUtils.copyProperties(userCreateFormDto, user, "username", "password");
        String fullname = VNCharacterUtils.removeAccent(convertFullNameToUsername(user.getFullName()));
        user.setUsername(autoGenerateUniqueUsername(fullname));
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 10;
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            sb.append(randomChar);
        }
        String randomString = sb.toString();
        user.setPassword(passwordEncoder.encode(randomString));
        EmailSendUserDto emailSendUserDto = new EmailSendUserDto();
        emailSendUserDto.setEmail(user.getEmail());
        emailSendUserDto.setPassword(randomString);
        emailSendUserDto.setUsername(user.getUsername());
        try {
            userService.createNew(user);
            sendEmailUser(emailSendUserDto);
            redirectAttributes.addFlashAttribute("messageSuccess", "message.0034");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageFail", "message.0035");
        }
        return "redirect:/admin/users";
    }


    @GetMapping("/users/{userId}")
    public String showViewUser(@PathVariable(name = "userId") Long id,
                               Model model) {
        User user = userService.findById(id).orElseThrow(EntityNotFoundException::new);

        UserDisplayDto userDisplayDto = new UserDisplayDto();
        BeanUtils.copyProperties(user, userDisplayDto);
        model.addAttribute("userDisplayDto", userDisplayDto);

        return "user/view-user";
    }

    @GetMapping("/users/edit/{userId}")
    public String showUpdateUsers(@PathVariable(name = "userId") Long id, Model model) throws IOException {
        User user = userService.findById(id).orElseThrow(EntityNotFoundException::new);

        UserParamDto userParamDto = new UserParamDto();
        BeanUtils.copyProperties(user, userParamDto);
        model.addAttribute("userParamDto", userParamDto);

        return "user/user-update";
    }

    @Transactional
    @PostMapping("/users/edit/{userId}")
    public String updateUser(@PathVariable(name = "userId") Long id,
                             @ModelAttribute("userParamDto") @Valid UserParamDto userParamDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) throws IOException {

        User user = userService.findById(id).orElseThrow(EntityNotFoundException::new);

        if (bindingResult.hasErrors()) {
            return "user/user-update";
        }
        BeanUtils.copyProperties(userParamDto, user, "id", "email");
        try {
        userService.update(user);
            redirectAttributes.addFlashAttribute("messageSuccess", "message.0039");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageFail", "message.0040");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users")
    public String userList(UserSearchParamDto searchParamDto, Model model) {
        Specification<User> basicConditions = userService.undeletedSpec();

        if (StringUtils.hasLength(searchParamDto.getSearchKey())) {
            basicConditions = basicConditions.and(userService.buildSpecForSearch(searchParamDto.getSearchKey()));
        }

        if (StringUtils.hasLength(searchParamDto.getRole())
                && EnumUtils.isValidName(UserRole.class, searchParamDto.getRole())) {
            Specification<User> userCondition = (root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("userRole"), UserRole.valueOf(searchParamDto.getRole()));
            basicConditions = basicConditions.and(userCondition);
        }

        List<Sort.Order> orderList = buildSortByKey(searchParamDto.getSort());
        Page<User> userPage = userService.findAll(basicConditions,
                PageRequest.of(searchParamDto.getPage(), searchParamDto.getSize(), Sort.by(orderList))
        );
        model.addAttribute("searchKey", searchParamDto.getSearchKey());
        model.addAttribute("role", searchParamDto.getRole());
        if (userPage.isEmpty()) {
            model.addAttribute("contentPage", Page.empty());
            return "user/list-user";
        }
        List<UserListDto> userListDto = userPage.getContent().stream()
                .map(User::convertToDisplayDto).collect(Collectors.toList());

        Page<UserListDto> contentPage = new PageImpl<>(userListDto,
                PageRequest.of(searchParamDto.getPage(), searchParamDto.getSize(), Sort.by(orderList)),
                userPage.getTotalElements());
        model.addAttribute("contentPage", contentPage);
        model.addAttribute("sort", searchParamDto.getSort());
        return "user/list-user";
    }

    private void sendEmailUser(EmailSendUserDto emailSendUserDto) {
        Context context = new Context();
        context.setVariable("emailSendUserDto", emailSendUserDto);
        String contentTemplate = springTemplateEngine.process("email-template/user-email", context);

        EmailDto emailSend = new EmailDto();

        //Set email Candidate//        emailSend.setEmailToList(Collections.singletonList(interviewSchedule.getCandidate().getEmail()));
        emailSend.setEmailToList(Collections.singletonList(emailSendUserDto.getEmail()));

        //Set Subject:
        emailSend.setSubject("[IMS][Account]Register success <"+ emailSendUserDto.getUsername() + ">");

        emailSend.setBody(contentTemplate);

        try {
            emailService.sendEmail(emailSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertFullNameToUsername(String fullName) {
        String[] arrFullName = fullName.split(" ");

        StringBuilder username = new StringBuilder("");
        username.append(arrFullName[arrFullName.length - 1]);
        for (int i = 0; i < arrFullName.length - 1; i++) {
            username.append(arrFullName[i].charAt(0));
        }
        return username.toString().toLowerCase();
    }

    private String autoGenerateUniqueUsername(String username) {
        StringBuilder finalUsername = new StringBuilder(username);

        LastUsernameDto lastUsernameDto = userService.getLastUsername(username);
        if (lastUsernameDto == null) {
            return finalUsername.toString();
        }
        String[] arrLastUserName = lastUsernameDto.getUsername().split(username);

        if (arrLastUserName.length == 0) {
            return finalUsername.append("1").toString();
        } else {
            long number = Long.parseLong(arrLastUserName[1]);
            return finalUsername.append(++number).toString();
        }
    }
}
