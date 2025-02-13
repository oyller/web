package com.kob.botrunningsystem;

import com.kob.botrunningsystem.service.Impl.BotRunningServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotrunningSystemApplication {

    public static void main(String[] args) {

        BotRunningServiceImpl.botPool.start();
        SpringApplication.run(BotrunningSystemApplication.class, args);
    }

}
