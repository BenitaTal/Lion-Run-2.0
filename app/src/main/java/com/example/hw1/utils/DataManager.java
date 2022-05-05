package com.example.hw1.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hw1.classes.GameManager;
import com.example.hw1.classes.PlayerLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {

    private static final String FILE_NAME = "GameDateFile";
    private static final String USERS_KEY = "usersDate";

    private SharedPreferences sharedPrefs;
    private HashMap<String, GameManager> users = new HashMap<>();
    private static DataManager myInstance = null;
    private GameManager user;

    private DataManager(Context ctx) {
        sharedPrefs = ctx.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        getUsersData();
    }

    public static void init(Context ctx){
        if (myInstance == null){
            myInstance = new DataManager(ctx);
        }
    }

    public static DataManager getInstance(){
        return myInstance;
    }

    public ArrayList<GameManager> getUsersData(){
        String json = sharedPrefs.getString(USERS_KEY, null);
        ArrayList<GameManager> currentUsers = null;
        if (json != null) {
            currentUsers = new Gson().fromJson(
                    json,
                    new TypeToken<ArrayList<GameManager>>()
                    {}.getType());
            for (GameManager user: currentUsers){
                users.put(user.getPlayerName(), user);
            }
        }
        return currentUsers;
    }

    public void updateUserData(GameManager user){
        users.put(user.getPlayerName(), user);
        ArrayList<GameManager> newUsersData = new ArrayList<>(users.values());
        String json = new Gson().toJson(
                newUsersData,
                new TypeToken<ArrayList<GameManager>>()
                {}.getType());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(USERS_KEY, json);
        editor.apply();
    }


    public GameManager getUserByName(String username){
        GameManager user = null;
        if (users != null){
            user = users.get(username);
        }
        return user;
    }
}
