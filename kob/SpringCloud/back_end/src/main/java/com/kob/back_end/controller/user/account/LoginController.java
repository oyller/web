package com.kob.back_end.controller.user.account;


import com.kob.back_end.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping  ("/user/account/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String> mp){
             return  loginService.login(mp.get("username"), mp.get("password"));
    }
}
