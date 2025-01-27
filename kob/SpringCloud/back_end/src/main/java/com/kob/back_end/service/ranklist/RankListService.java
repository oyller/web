package com.kob.back_end.service.ranklist;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;


public interface RankListService {
    public JSONObject Get_Rank_List(Integer page);
}
