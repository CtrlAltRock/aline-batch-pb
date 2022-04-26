package com.smoothstack.BatchMicroservice.generator;

import com.smoothstack.BatchMicroservice.cache.CardCache;
import com.smoothstack.BatchMicroservice.cache.UserCache;
import com.smoothstack.BatchMicroservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class GeneratorAndCacheTest {

    private final UserCache userCache = UserCache.getInstance();
    private final CardCache cardCache = CardCache.getInstance();

    @Test
    public void correctUserGenerationTest(){
        assertTrue(userCache.getGeneratedUsers().isEmpty());
        userCache.findUserOrGenerate(0L);
        assertEquals(1, userCache.getGeneratedUsers().size());
        User userOrGenerate = userCache.findUserOrGenerate(0L);
        assertEquals(userOrGenerate.getFirstName(), userCache.getGeneratedUser(0L).getFirstName());
        userCache.getGeneratedUsers().remove(0L);
        assertTrue(userCache.getGeneratedUsers().isEmpty());
    }

    @Test
    public void correctCardGenerationTest(){
        assertTrue(cardCache.getGeneratedCards().isEmpty());
        cardCache.findOrGenerateCard(0L, 2L);
        assertEquals(3, cardCache.getGeneratedUserCards(0L).size());
        cardCache.findOrGenerateCard(0L, 3L);
        assertEquals(4, cardCache.getGeneratedUserCards(0L).size());
        cardCache.getGeneratedCards().remove(0L);
        assertTrue(cardCache.getGeneratedCards().isEmpty());
    }
}
