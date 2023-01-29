package com.fa.ims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController {
    @GetMapping("/403")
    public String handleError403(){
        return "error/403";
    }

}
