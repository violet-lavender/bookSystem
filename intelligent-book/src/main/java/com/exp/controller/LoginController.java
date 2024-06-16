package com.exp.controller;

import com.exp.dto.LoginRequest;
import com.exp.dto.RegisterRequest;
import com.exp.pojo.Result;
import com.exp.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest){
        log.info("登录: {}", loginRequest);
        return loginService.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getRole());
    }

    @PostMapping("/register")
    public Result registerUser(@RequestBody RegisterRequest registerRequest) {
        log.info("注册: {}", registerRequest);
        return loginService.registerUser(registerRequest);
    }
}
