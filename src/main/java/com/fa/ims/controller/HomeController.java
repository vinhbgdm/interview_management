package com.fa.ims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/home-page")
    public String homePage() {
        return "/home-page";
    }

    @GetMapping("/")
    public String returnHomePage() {
        return "redirect:/home-page";
    }

}
