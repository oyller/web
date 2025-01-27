package com.kob.back_end.controller.pk;


import com.kob.back_end.service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class StartGameController {
    @Autowired
    private StartGameService startGameService;

    @PostMapping("/pk/start/game/")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id")));
        Integer a_bot_id = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_bot_id")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        Integer b_bot_id = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_bot_id")));
        System.out.println(aId + " " + a_bot_id + " " +  bId + " " +  b_bot_id +  " match success");
        return startGameService.startGame(aId, bId, a_bot_id, b_bot_id);
    }
}
