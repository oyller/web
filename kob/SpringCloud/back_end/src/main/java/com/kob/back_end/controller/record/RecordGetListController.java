package com.kob.back_end.controller.record;

import com.alibaba.fastjson2.JSONObject;
import com.kob.back_end.service.record.Record_GetList_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class RecordGetListController {

      @Autowired
      Record_GetList_Service recordGetListService;

      @GetMapping ("/record/record_page_get")
      JSONObject Get_page_List(@RequestParam Map<String, String> data)
      {
            Integer page = Integer.valueOf(data.get("page"));
            return recordGetListService.record_getlist(Integer.valueOf(data.get("page")));
      }

}
