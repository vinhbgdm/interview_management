package com.fa.ims.controller;

import com.fa.ims.dto.UserLoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController extends BaseController {

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "auth/login";
    }

}
