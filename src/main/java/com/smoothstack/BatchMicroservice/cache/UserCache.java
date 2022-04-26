package com.smoothstack.BatchMicroservice.cache;

import com.smoothstack.BatchMicroservice.generator.UserGenerator;
import com.smoothstack.BatchMicroservice.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserCache {

    private final HashMap<Long, User> generatedUsers = new HashMap<>();
    private final Map<Long, User> syncGeneratedUsers = Collections.synchronizedMap(generatedUsers);
    private final HashSet<Long> seenUsers = new HashSet<>();
    private final Set<Long> syncSeenUsers = Collections.synchronizedSet(seenUsers);
    private static UserCache userCacheInstance = null;

    public static UserCache getInstance() {
        if(userCacheInstance == null) userCacheInstance = new UserCache();
        return userCacheInstance;
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

    public Set<Long> getSeenUsers(){
        return syncSeenUsers;
    }

    public void setSeenUser(Long id){
        syncSeenUsers.add(id);
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
