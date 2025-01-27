package com.kob.back_end.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.back_end.mapper.botMapper;
import com.kob.back_end.pojo.User;
import com.kob.back_end.pojo.bot;
import com.kob.back_end.service.impl.utils.UserDetailsImpl;
import com.kob.back_end.service.user.bot.GetlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class GetlistServiceImpl implements GetlistService {
    @Autowired
    private botMapper botMapper;

    @Override
    public List<bot> Getlist() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        Integer id = user.getId();
        QueryWrapper<bot> queryWrapper = new QueryWrapper<bot>();
        queryWrapper.eq("user_id", id);
        List<bot> bots = botMapper.selectList(queryWrapper);
        return bots;
    }
}
