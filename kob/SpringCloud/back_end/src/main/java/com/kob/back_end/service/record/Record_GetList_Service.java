package com.kob.back_end.service.record;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;


public interface Record_GetList_Service {
     public JSONObject record_getlist(Integer page);
}
