package com.kob.back_end.service.impl.pk;

import com.kob.back_end.consumer.WebSocketServer;
import com.kob.back_end.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer bId, Integer a_bot_id, Integer b_bot_id) {
        WebSocketServer.startGame(aId, bId, a_bot_id, b_bot_id);
        return "start game success";
    }
}
