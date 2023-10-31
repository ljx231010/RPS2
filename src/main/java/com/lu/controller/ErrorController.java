package com.lu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping("/404")
    public String page404() {
        System.out.println("ErrorController+404");
        return "/404";
    }

    @RequestMapping("/500")
    public String page500() {
        return "error/405";
    }
}
