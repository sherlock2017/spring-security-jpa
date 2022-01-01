package com.learning.controller;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping({"/login"})
    public String login(){
        return "login";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(){
        return "accessDenied";
    }
}
