package com.kob.back_end.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.back_end.consumer.WebSocketServer;
import com.kob.back_end.pojo.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;


public class Game extends Thread {
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    final private int[][] g;
    final private static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private final Player playerA, playerB;
    private ReentrantLock lock = new ReentrantLock();
    final private  Integer ida;
    final private  Integer idb;
    private  Integer nextStepA = null, nextStepB = null;
    final Integer [] dr = new Integer[] {-1, 0, 1, 0};
    final Integer [] dc = new Integer[] {0, 1, 0, -1};
    private String status = "playing";
    private String loser = "";

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer user_a, Integer user_b) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        this.ida = user_a;
        this.idb = user_b;
        playerA = new Player(user_a, rows - 2, 1, new ArrayList<>());
        playerB = new Player(user_b, 1, cols - 2, new ArrayList<>());
    }

    public int[][] getG() {
        return g;
    }

    public Player getPlayerA(){
         return playerA;
    }

    public Player getPlayerB(){
        return playerB;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i ++ ) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    private boolean draw() {  // 画地图
        for (int i = 0; i < this.rows; i ++ ) {
            for (int j = 0; j < this.cols; j ++ ) {
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i ++ ) {
            for (int j = 0; j < 1000; j ++ ) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i ++ ) {
            if (draw())
                break;
        }
    }

    public void setNextStepA(Integer nextStepA)
    {
          lock.lock();
          try{
               this.nextStepA = nextStepA;
          }finally {
              lock.unlock();
          }
    }

    public void setNextStepB(Integer nextStepB)
    {
        lock.lock();
        try{
            this.nextStepB = nextStepB;
        }finally {
            lock.unlock();
        }
    }


    private boolean nextStep(){
            try{
                Thread.sleep(200);
            }catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < 50; i++)
            {
                try{
                    Thread.sleep(400);
                    lock.lock();
                    try{
                        if (nextStepA != null && nextStepB != null) {
                            playerA.getSteps().add(nextStepA);
                            playerB.getSteps().add(nextStepB);
                            return true;
                        }
                    }finally {
                        lock.unlock();
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return false;
    }

    public boolean check_valid(List<Cell> cellsA, List<Cell> cellsB)
    {
         int n = cellsA.size();
         Cell cell = cellsA.get(n - 1);
         if (g[cell.x][cell.y] == 1) return false;
         for (int i = 0; i < n - 1; i++)
         {
                if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y) return false;
         }
         for (int i = 0; i < n - 1; i++)
         {
                if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y) return false;
         }
         return true;
    }

    public void judge(){
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();
        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }


    }

    private void sendAllMessage(String message) {
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i ++ ) {
            for (int j = 0; j < cols; j ++ ) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }


    private void saveToDatabase(){
        record rd = new record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        System.out.println(rd);
        WebSocketServer.recordmapper.insert(rd);
    }


    private void sendResult() {  // 向两个Client公布结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }


    @Override
    public void run(){
           for (int i = 0; i < 1000; i++){
                if (nextStep())
                {
                         judge();
                          if (this.status.equals("playing")) {
                              JSONObject respA = new JSONObject();
                              respA.put("event", "move");
                              respA.put("opponent_direction", nextStepB.toString());
                              respA.put("owner_direction", nextStepA.toString());
                              WebSocketServer.users.get(playerA.getId()).sendMessage(respA.toJSONString());
                              JSONObject respB = new JSONObject();
                              respB.put("event", "move");
                              respB.put("opponent_direction", nextStepA.toString());
                              respB.put("owner_direction", nextStepB.toString());
                              WebSocketServer.users.get(playerB.getId()).sendMessage(respB.toJSONString());
                              nextStepB = null;
                              nextStepA = null;
                          }
                          else if (this.status.equals("finished"))
                          {
                                sendResult();
                                break;
                          }
                }
                else
                {
                    status = "finished";
                    lock.lock();
                    try {
                        if (nextStepA == null && nextStepB == null) {
                            loser = "all";
                        } else if (nextStepA == null) {
                            loser = "A";
                        } else {
                            loser = "B";
                        }
                    } finally {
                        lock.unlock();
                    }
                    sendResult();
                    break;
                }
           }
    }

}
