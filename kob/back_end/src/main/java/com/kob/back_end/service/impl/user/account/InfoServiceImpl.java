package com.kob.back_end.service.impl.user.account;

import com.kob.back_end.service.impl.utils.UserDetailsImpl;
import com.kob.back_end.service.user.account.InfoServcie;
import com.kob.back_end.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kob.back_end.pojo.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoServcie {

    @Override
    public Map<String, String> getinfo() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        return map;
    }
}
