package com.kob.back_end.controller.user.bot;

import com.kob.back_end.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddController {

    @Autowired
    private AddService addService;

    @PostMapping("/user/bot/add/")
    Map<String, String> add(@RequestParam Map<String, String> mp) {
        return addService.add(mp);
    }
}
