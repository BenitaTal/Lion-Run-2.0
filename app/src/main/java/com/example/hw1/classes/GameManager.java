package com.example.hw1.classes;

import android.util.Log;

import com.google.gson.Gson;

public class GameManager implements Comparable<GameManager>{

    private final int NUM_OF_LIVES = 3;
    private int lives = NUM_OF_LIVES;
    private int score;
    private String playerName;
    private PlayerLocation location;


    public GameManager(){
        setPlayerName(playerName);
        setScoreStart();
        this.location = new PlayerLocation();
    }
    public void setGameManager(GameManager gameManager){
        this.playerName = gameManager.getPlayerName();
        this.score = gameManager.getScore();
        this.location = gameManager.getLocation();
    }

    public void setScoreStart(){
        this.score = 0;
    }
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setUserLocation(double lon, double lat, String addressName) {
        setLocation(new PlayerLocation(lon, lat, addressName));
    }

    public PlayerLocation getLocation() {
        return location;
    }

    public void setLocation(PlayerLocation location) {
        this.location = location;
    }

    public int getScore(){
        return this.score;
    }
    public int getLives() {
        return this.lives;
    }

    public void reduceLives() {
       this.lives--;
    }

    public void ScoreUpByOne() {
        this.score++;
    }

    public void ScoreUpByTen() {
        this.score+=10;
    }

    public void ScoreReduceByFive() {
        this.score-=5;
    }


    public String userToJson(){
        try{
            Gson gson = new Gson();
            return gson.toJson(this);
        }catch (Exception e){
            Log.e("ERROR","Failed to parse from User to json\n" + e.getMessage());
        }
        return "";
    }



    public static GameManager fromJsonToUser(String json){
        GameManager user = new GameManager();
        try{
            Gson gson = new Gson();
            user = gson.fromJson(json, GameManager.class);
        }catch (Exception e){
            Log.e("ERROR","Failed to parse from json to User\n" + e.getMessage());
        }
        return user;
    }

    @Override
    public int compareTo(GameManager user) {
        if (user.score > this.score){
            return 1;
        }else if(user.score < this.score){
            return -1;
        }
        return  0;
    }

}

