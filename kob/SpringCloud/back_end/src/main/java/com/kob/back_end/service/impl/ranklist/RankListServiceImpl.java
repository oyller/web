package com.kob.back_end.service.impl.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.back_end.mapper.UserMapper;
import com.kob.back_end.pojo.User;
import com.kob.back_end.service.ranklist.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankListServiceImpl implements RankListService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject Get_Rank_List(Integer page) {
        IPage<User> userIPage = new Page<>(page, 10);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users = userMapper.selectPage(userIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        for (User user:users)
        {
            user.setPassword("");
        }
        resp.put("players", users);
        resp.put("players_count",  userMapper.selectCount(null));
        return resp;
    }
}
