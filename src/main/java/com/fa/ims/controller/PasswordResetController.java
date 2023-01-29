package com.fa.ims.controller;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.dto.EmailDto;
import com.fa.ims.dto.ForgotPasswordDto;
import com.fa.ims.entity.PasswordResetToken;
import com.fa.ims.entity.User;
import com.fa.ims.security.SecurityUtil;
import com.fa.ims.service.PasswordResetTokenService;
import com.fa.ims.service.UserService;
import com.fa.ims.service.impl.SendGridMailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final SendGridMailService sendGridMailService;
    private final SpringTemplateEngine springTemplateEngine;

    @Value("${app.portal-urL}")
    private String portalUrl;
    public PasswordResetController(UserService userService, PasswordResetTokenService passwordResetTokenService,
                                   SendGridMailService sendGridMailService, SpringTemplateEngine springTemplateEngine) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.sendGridMailService = sendGridMailService;
        this.springTemplateEngine = springTemplateEngine;
    }

    @GetMapping("/forgot-password")
    public String showForgotPassword(Model model) {
        model.addAttribute("forgotPasswordDto", new ForgotPasswordDto());
        return "/auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String getForgotPassword(@ModelAttribute ForgotPasswordDto forgotPasswordDto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    HttpServletRequest request,
                                    Model model) throws IOException {
        Optional<User> userOptional = userService.findByDeletedFalseAndEmail(forgotPasswordDto.getEmail());
        if (userOptional.isEmpty()) {
            bindingResult.rejectValue("email", "Message.016");
            return "/auth/forgot-password";
        }

        if (!StringUtils.hasLength(userOptional.get().getEmail())) {
            bindingResult.rejectValue("email", "Message.019");
            return "/auth/forgot-password";
        }

        User user = userOptional.get();

        //release a token
        PasswordResetToken passwordResetToken = PasswordResetToken
                .builder()
                .token(UUID.randomUUID().toString())
                .expiryTime(
                        LocalDateTime.now().plusMinutes(CommonConstants.RESET_PASSWORD_EXPIRATION_MINUTE)
                )
                .user(user)
                .build();
        passwordResetTokenService.createNew(passwordResetToken);
        //sent email
        List<String> todoList = new ArrayList<>();
        todoList.add(user.getEmail());
        String subject = "[IMS]["+ LocalDate.now().format(DateTimeFormatter.ofPattern(CommonConstants.FORMAT_DATE))+"] " + "Reset password confirmation";
        //setup dump html from view
        Context context = new Context();
        String url = portalUrl + "/reset-password?token=" + passwordResetToken.getToken();
        context.setVariable("passwordResetUrl", url);
        context.setVariable("passwordResetToken",passwordResetToken);
        String contentTemplate = springTemplateEngine.process("auth/reset-password-email", context);

        sendGridMailService.sendEmail(
                EmailDto.builder()
                        .emailToList(todoList)
                        .subject(subject)
                        .body(contentTemplate)
                        .build()
        );
        redirectAttributes.addFlashAttribute("sendEmail",
                "Message.018");
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam(name = "token") String token,
                                    Model model) {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
        Optional<PasswordResetToken> passwordResetTokenOptional = null;
        try {
            passwordResetTokenOptional = passwordResetTokenService.findByDeletedFalseAndToken(token);
        } catch (EntityNotFoundException e) {
            model.addAttribute("forgotPasswordDto", forgotPasswordDto);
            return "/auth/reset-password";
        }

        //validate token
        forgotPasswordDto.setValidatedToken(validateToken(token, passwordResetTokenOptional) );

        if (forgotPasswordDto.getValidatedToken() == null) {
            model.addAttribute("forgotPasswordDto", forgotPasswordDto);
            return "/auth/reset-password";
        } else {
            forgotPasswordDto.setEmail(
                    passwordResetTokenOptional.get().getUser().getEmail()
            );
            model.addAttribute("forgotPasswordDto", forgotPasswordDto);

            return "/auth/reset-password";
        }
    }

    private String validateToken(String token, Optional<PasswordResetToken> passwordResetTokenOptional) {
        if (passwordResetTokenOptional.isEmpty()) return null;
        else if (passwordResetTokenOptional
                .get()
                .getExpiryTime()
                .isBefore(LocalDateTime.now())) {
            return null;
        }
        else return token;
    }

    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute @Valid ForgotPasswordDto forgotPasswordDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (!bindingResult.hasErrors()) {
            if (!forgotPasswordDto.getPassword().equals(forgotPasswordDto.getConfirmPassword()))
                bindingResult.rejectValue("confirmPassword", "Message.020");
        }

        if (bindingResult.hasErrors()) {
            return "/auth/reset-password";
        }
        //reset password
        updateResetPasswordToken(forgotPasswordDto);
        //reset all token
        passwordResetTokenService.findAll().forEach(oldToken -> {
            oldToken.setDeleted(true);
            passwordResetTokenService.update(oldToken);
        });
        redirectAttributes.addFlashAttribute("messageResetPassword","Message.021");
        return "redirect:/auth/login";
    }

    @Transactional()
    public void updateResetPasswordToken(ForgotPasswordDto forgotPasswordDto) {
        Optional<User> userOptional = userService.findByDeletedFalseAndEmail(forgotPasswordDto.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(
                    SecurityUtil.encryptPasswordByBcryptEncoder(forgotPasswordDto.getPassword())
            );
            userService.update(user);
        }
    }
}