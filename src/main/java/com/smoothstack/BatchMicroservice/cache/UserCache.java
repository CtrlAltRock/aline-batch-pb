package com.smoothstack.BatchMicroservice.cache;

import com.smoothstack.BatchMicroservice.model.User;

import java.util.HashMap;

public class UserCache {

    private HashMap<Long, User> generatedUsers = new HashMap<>();

    public void addGeneratedUser(Long userId, User user){
        generatedUsers.put(userId, user);
    }

    public User getGeneratedUser(Long userId){
        return generatedUsers.get(userId);
    }
}
