package com.kob.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Integer userId, Integer rating, Integer bot_id);
    String removePlayer(Integer userId);

}
