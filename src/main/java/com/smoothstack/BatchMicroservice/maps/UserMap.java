package com.smoothstack.BatchMicroservice.maps;

import com.smoothstack.BatchMicroservice.generator.UserGenerator;
import com.smoothstack.BatchMicroservice.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserMap {

    private final HashMap<Long, User> generatedUsers = new HashMap<>();
    private final Map<Long, User> syncGeneratedUsers = Collections.synchronizedMap(generatedUsers);
    private final HashMap<Long, Integer> insufficientBalanceByUser = new HashMap<>();

    private static final class UserMapInstanceHolder {
        static final UserMap userMapInstance = new UserMap();
    }

    public static UserMap getInstance() {
        return UserMapInstanceHolder.userMapInstance;
    }

    public synchronized void addGeneratedUser(Long userId, User user){
        syncGeneratedUsers.put(userId, user);
    }

    public User getGeneratedUser(Long userId){
        return syncGeneratedUsers.get(userId);
    }

    public Map<Long, User> getGeneratedUsers(){
        return syncGeneratedUsers;
    }

    public HashMap<Long, Integer> getInsufficientBalanceByUser(){
        return insufficientBalanceByUser;
    }

    public void setInsufficientBalanceByUser(Long id){
        if(!insufficientBalanceByUser.containsKey(id)) {
            insufficientBalanceByUser.put(id, 1);
        } else {
            Integer integer = insufficientBalanceByUser.get(id);
            integer = integer + 1;
            insufficientBalanceByUser.replace(id, integer);
        }
    }

    public User findUserOrGenerate(Long userId){
        UserGenerator userGenerator = UserGenerator.getInstance();
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
