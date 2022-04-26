package com.smoothstack.BatchMicroservice.generator;

import com.smoothstack.BatchMicroservice.cache.CardCache;
import com.smoothstack.BatchMicroservice.cache.MerchantCache;
import com.smoothstack.BatchMicroservice.cache.UserCache;
import com.smoothstack.BatchMicroservice.model.Merchant;
import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GeneratorAndCacheTest {

    private final UserCache userCache = UserCache.getInstance();
    private final CardCache cardCache = CardCache.getInstance();
    private final MerchantCache merchantCache = MerchantCache.getInstance();

    @Test
    public void correctUserGenerationTest(){
        assertTrue(userCache.getGeneratedUsers().isEmpty());
        assertSame(userCache.findUserOrGenerate(0L).getClass(), User.class);
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

    @Test
    public void correctMerchantGenerationTest(){
        Transaction t = new Transaction();
        t.setMerchant_name("1092875012421");
        t.setMerchant_state("VA");
        t.setMerchant_city("Arlington");
        t.setMerchant_zip("20350");
        t.setMcc("5111");
        assertTrue(merchantCache.getGeneratedMerchants().isEmpty());
        assertSame(merchantCache.findOrGenerateMerchant(t).getClass(), Merchant.class);
        assertEquals(1, merchantCache.getGeneratedMerchants().size());
        Merchant merchant = merchantCache.findOrGenerateMerchant(t);
        assertEquals(merchant.getName(), merchantCache.getGeneratedMerchants().get(t.getMerchant_name()).getName());
        merchantCache.getGeneratedMerchants().remove(t.getMerchant_name());
        assertTrue(merchantCache.getGeneratedMerchants().isEmpty());
    }
}
