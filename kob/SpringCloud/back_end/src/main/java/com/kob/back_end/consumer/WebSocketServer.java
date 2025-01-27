package com.kob.back_end.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.back_end.config.filter.JwtAuthenticationTokenFilter;
import com.kob.back_end.mapper.UserMapper;
import com.kob.back_end.mapper.botMapper;
import com.kob.back_end.mapper.recordMapper;
import com.kob.back_end.pojo.User;
import com.kob.back_end.pojo.bot;
import com.kob.back_end.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kob.back_end.consumer.utils.Game;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    public final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private Session session = null;
    private User user;
    public static UserMapper userMapper;
    public static RestTemplate restTemplate;
    public static recordMapper recordmapper;
    private static botMapper bot_Mapper;

    public Game game = null;

    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerurl = "http://127.0.0.1:3001/player/remove/";


    @Autowired
    public void setUserMapper(UserMapper userMapper){
         WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(recordMapper recordMapper) {
        WebSocketServer.recordmapper = recordMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @Autowired
    public void setbotMapper(botMapper botMapper) {
        WebSocketServer.bot_Mapper = botMapper;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        Integer userid = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        User user = userMapper.selectById(userid);
        this.user = user;
        if (this.user != null)
        {
            users.put(userid, this);
        }
        else
        {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
         if (this.user != null) {
              users.remove(this.user.getId());

         }
    }

    public static void startGame(Integer aId, Integer bId, Integer a_bot_id, Integer b_bot_id)
    {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        bot botA = bot_Mapper.selectById(a_bot_id), botB = bot_Mapper.selectById(b_bot_id);
        Game game = new Game(13, 14, 20, a.getId(), botA, b.getId(), botB);
        game.createMap();
        users.get(a.getId()).game = game;
        users.get(b.getId()).game = game;
        game.start();
        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("opponent_id", b.getId());
        respA.put("gamemap", game.getG());
        respA.put("owner_snake_id", "0");
        respA.put("opponent_snake_id", "1");
        respA.put("play_id", "A");
        users.get(a.getId()).sendMessage(respA.toJSONString());
        JSONObject respB = new JSONObject();
        respB.put("opponent_id", a.getId());
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("gamemap", game.getG());
        respB.put("owner_snake_id", "1");
        respB.put("opponent_snake_id", "0");
        respB.put("play_id", "B");
        users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    public void startMatching(String bot_id)
    {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id", bot_id);
        System.out.println(data);
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    public void stopMatching()
    {
        MultiValueMap<String, String>data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayerurl, data, String.class);
    }


    private void move(int direction){
        if (game.getPlayerA().getId().equals(user.getId())) {
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event))
        {
              startMatching(data.getString("bot_id"));
        }
        else if ("stop-matching".equals(event))
        {
              stopMatching();
        }
        else if ("move".equals(event))
        {
               move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
           synchronized (this.session) {
            try{
                this.session.getBasicRemote().sendText(message);
            }catch (IOException e)
            {
               e.printStackTrace();
            }
           }
    }

}

