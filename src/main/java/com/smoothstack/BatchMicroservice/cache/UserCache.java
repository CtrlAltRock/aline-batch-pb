package com.smoothstack.BatchMicroservice.cache;

import com.smoothstack.BatchMicroservice.generator.UserGenerator;
import com.smoothstack.BatchMicroservice.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Component
public class UserCache {

    private final HashMap<Long, User> generatedUsers = new HashMap<>();

    private final UserGenerator userGenerator = UserGenerator.getInstance();


    public synchronized void addGeneratedUser(Long userId, User user){
        generatedUsers.put(userId, user);
    }

    public User getGeneratedUser(Long userId){
        return generatedUsers.get(userId);
    }

    public HashMap<Long, User> getGeneratedUsers(){
        return generatedUsers;
    }

    public User findUserOrGenerate(Long userId){
        if(getGeneratedUser(userId) == null){
            synchronized (UserGenerator.class){
                if(getGeneratedUser(userId) == null){
                    userGenerator.generateUser(userId, this);
                }
            }
        }
        return getGeneratedUser(userId);
    }

}
