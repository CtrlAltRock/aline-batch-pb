package com.smoothstack.BatchMicroservice.generator;

import com.github.javafaker.Faker;
import com.smoothstack.BatchMicroservice.maps.UserMap;
import com.smoothstack.BatchMicroservice.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserGenerator {

    private final Faker faker = new Faker();

    private static final class UserGeneratorInstanceHolder {
        static final UserGenerator userGeneratorInstance = new UserGenerator();
    }
    public static UserGenerator getInstance() {
        return UserGeneratorInstanceHolder.userGeneratorInstance;
    }

    public synchronized User generateUser(Long userId, UserMap uc) {
//        TODO - Optimize with on call generator
        String firstName= faker.name().firstName();
        String lastName= faker.name().lastName();
        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(firstName + "." + lastName + "@smoothceeplusplus.com");
        user.setCards(new ArrayList<>());
        user.setTransactions(new ArrayList<>());
        uc.addGeneratedUser(userId, user);
//        System.out.println(user);
        return user;
    }

}
