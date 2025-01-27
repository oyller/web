package com.kob.back_end.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.back_end.mapper.botMapper;
import com.kob.back_end.pojo.bot;
import com.kob.back_end.pojo.User;
import com.kob.back_end.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import com.kob.back_end.service.impl.utils.UserDetailsImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private botMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> mp) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Integer id = Integer.parseInt(mp.get("id"));
        System.out.println(mp.get("id"));
        Map<String, String> map = new HashMap<>();
        bot bot = botMapper.selectById(id);
        if (bot == null)
        {
            map.put("error_message", "Bot不存在或已被删除");
            return map;
        }
        if (!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限删除该Bot");
            return map;
        }
        botMapper.deleteById(id);
        map.put("error_message", "success");
        return map;
    }
}
