package com.learning.controller;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping({"/login"})
    public String login(){
        return "login";
    }

    @GetMapping({"/","/home"})
    public String home(){
        return "home";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(){
        return "accessDenied";
    }

    @GetMapping(value="/logout")
    public String getLogoutPage(Model model){
        return "logout";
    }

    @GetMapping("/success")
    public String success(Model model){
        model.addAttribute("message", "Success!!");
        return "message";
    }

    @GetMapping("/fail")
    public String fail(Model model){
        model.addAttribute("message", "Failure!!");
        return "message";
    }

}
