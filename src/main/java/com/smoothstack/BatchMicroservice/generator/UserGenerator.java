package com.smoothstack.BatchMicroservice.generator;

import com.github.javafaker.Faker;
import com.smoothstack.BatchMicroservice.cache.UserCache;
import com.smoothstack.BatchMicroservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserGenerator {

    private final Faker faker = new Faker();
    private final UserCache userCache = new UserCache();
    private User user;

    public User findUserOrGenerate(Long userId){
        if(userCache.getGeneratedUser(userId) == null){
            generateUser(userId);
            userCache.addGeneratedUser(userId, user);
        }
        return userCache.getGeneratedUser(userId);
    }

    private synchronized void generateUser(Long userId) {
        String firstName= faker.name().firstName();
        String lastName= faker.name().firstName();
        user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(firstName + "." + lastName + "@random.com");
    }
}
