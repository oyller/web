package com.kob.back_end.controller.user.bot;

import com.kob.back_end.pojo.bot;
import com.kob.back_end.service.user.bot.GetlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetlistController {
    @Autowired
    private GetlistService getlistService;

    @GetMapping("/user/bot/getlist")
    private List<bot> getlist(){
        return getlistService.Getlist();
    }
}
