package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class HelloController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        log.info("home controller called");
        return "home"; // home html로 보내겠다
    }
}
