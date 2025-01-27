package com.kob.back_end.controller.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.kob.back_end.service.ranklist.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RankListController {
    @Autowired
    private RankListService rankListService;
    @GetMapping("/rank/ranklist_page_get/")
    JSONObject Get_page_List(@RequestParam Map<String, String> data)
    {
        Integer page = Integer.valueOf(data.get("page"));
        return rankListService. Get_Rank_List(Integer.valueOf(data.get("page")));
    }
}
