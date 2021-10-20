package com.chapjun.secondservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/second-service")
@Slf4j
public class SecondServiceController {

    final Environment env;

    @Autowired
    public SecondServiceController(Environment env) {
        this.env = env;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome to the second service.";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request") String header) {
        log.info("header : " + header);
        return "Hello World in second Service";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {

        log.info("Server Port : ${}", request.getServerPort());
        return String.format("Hi, there. This is a message from Second Service ON PORT : %s",
                env.getProperty("local.server.port"));
    }
}
