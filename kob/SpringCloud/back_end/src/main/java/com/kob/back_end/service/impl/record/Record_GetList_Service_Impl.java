package com.kob.back_end.service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.back_end.mapper.UserMapper;
import com.kob.back_end.mapper.recordMapper;
import com.kob.back_end.pojo.User;
import com.kob.back_end.pojo.record;
import com.kob.back_end.service.record.Record_GetList_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


@Service
public class Record_GetList_Service_Impl implements Record_GetList_Service {

    @Autowired
    private recordMapper record_Mapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject record_getlist(Integer page) {
        IPage<record> recordIPage = new Page<>(page, 10);
        QueryWrapper<record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<record> records = record_Mapper.selectPage(recordIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for (record r:records)
        {
             User userA = userMapper.selectById(r.getAId());
             User userB = userMapper.selectById(r.getBId());
             JSONObject item = new JSONObject();
             item.put("a_photo", userA.getPhoto());
             item.put("a_username", userA.getUsername());
             item.put("b_photo", userB.getPhoto());
             item.put("b_username", userB.getUsername());
             String result = "平局";
             if ("A".equals(r.getLoser())) result = "B胜";
             else if ("B".equals(r.getLoser())) result = "A胜";
             item.put("result", result);
             item.put("record", r);
             items.add(item);
        }
        resp.put("records", items);
        resp.put("records_count", record_Mapper.selectCount(null));
        return resp;
    }
}
