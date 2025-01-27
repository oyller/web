package com.kob.back_end.controller.user.bot;


import com.kob.back_end.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateController {
    @Autowired
    private  UpdateService updateService;
    @PostMapping("/user/bot/update/")
    Map<String, String> update(@RequestParam Map<String, String> mp)
    {
        System.out.println(mp);
        return updateService.update(mp);
    }
}
