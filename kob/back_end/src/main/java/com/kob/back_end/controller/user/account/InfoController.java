package com.kob.back_end.controller.user.account;

import com.kob.back_end.service.user.account.InfoServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private InfoServcie infoServcie;

    @GetMapping("/user/account/info")
    public Map<String, String> getinfo(){
         return infoServcie.getinfo();
    }
}
