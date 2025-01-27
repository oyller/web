package com.kob.back_end.controller.user.account;

import com.kob.back_end.service.impl.user.account.RegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    private RegisterServiceImpl registerService;

    @PostMapping("/user/account/register")
    public Map<String, String> register(@RequestParam Map<String, String> mp){
         System.out.println(mp.get("username") + " " + mp.get("password"));
         return  registerService.register(mp.get("username"), mp.get("password"), mp.get("confirmedPassword"));
    }
}
