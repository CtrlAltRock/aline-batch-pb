package com.smoothstack.BatchMicroservice;

import com.smoothstack.BatchMicroservice.maps.*;
import com.smoothstack.BatchMicroservice.model.generation.Merchant;
import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.generation.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(
        properties = {
                "input.path = C:/Projects/Smoothstack/Assignments/Sprints/AlineFinancial/aline-batch-microservice/src/test/resources/TestData/test2.csv",
                "output.path = C:/Projects/Smoothstack/Assignments/Sprints/AlineFinancial/aline-batch-microservice/src/test/ProcessedOutTestFiles/"
        })
public class GeneratorAndCacheTest {

    private final UserMap userMap = UserMap.getInstance();
    private final CardMap cardMap = CardMap.getInstance();
    private final MerchantMap merchantMap = MerchantMap.getInstance();
    private final LocationMap locationMap = LocationMap.getInstance();
    private final StateMap stateMap = StateMap.getInstance();

    @Test
    public void correctUserGenerationTest(){
        assertTrue(userMap.getGeneratedUsers().isEmpty());
        assertSame(userMap.findUserOrGenerate(0L).getClass(), User.class);
        assertEquals(1, userMap.getGeneratedUsers().size());
        User userOrGenerate = userMap.findUserOrGenerate(0L);
        assertEquals(userOrGenerate.getFirstName(), userMap.getGeneratedUser(0L).getFirstName());
        userMap.getGeneratedUsers().remove(0L);
        assertTrue(userMap.getGeneratedUsers().isEmpty());
    }

    @Test
    public void correctCardGenerationTest(){
        assertTrue(cardMap.getGeneratedCards().isEmpty());
        cardMap.findOrGenerateCard(0L, 2L);
        assertEquals(3, cardMap.getGeneratedUserCards(0L).size());
        cardMap.findOrGenerateCard(0L, 3L);
        assertEquals(4, cardMap.getGeneratedUserCards(0L).size());
        cardMap.getGeneratedCards().remove(0L);
        assertTrue(cardMap.getGeneratedCards().isEmpty());
    }

    @Test
    public void correctMerchantGenerationTest(){
        Transaction t = new Transaction();
        t.setMerchant_name("1092875012421");
        t.setMerchant_state("VA");
        t.setMerchant_city("Arlington");
        t.setMerchant_zip("20350");
        t.setMcc("5111");
        assertTrue(merchantMap.getGeneratedMerchants().isEmpty());
        assertSame(merchantMap.findOrGenerateMerchant(t).getClass(), Merchant.class);
        assertEquals(1, merchantMap.getGeneratedMerchants().size());
        Merchant merchant = merchantMap.findOrGenerateMerchant(t);
        assertEquals(merchant.getId(), merchantMap.getGeneratedMerchants().get(t.getMerchant_name()).getId());
        merchantMap.getGeneratedMerchants().remove(t.getMerchant_name());
        assertTrue(merchantMap.getGeneratedMerchants().isEmpty());
    }

    @Test
    public void correctLocationAndStateTest(){
        Transaction t = new Transaction();
        t.setMerchant_name("1092875012421");
        t.setMerchant_state("VA");
        t.setMerchant_city("Arlington");
        t.setMerchant_zip("20350");
        t.setMcc("5111");
        assertTrue(merchantMap.getGeneratedMerchants().isEmpty());
        assertTrue(locationMap.getGeneratedLocations().isEmpty());
        assertTrue(stateMap.getGeneratedStates().isEmpty());
        assertSame(merchantMap.findOrGenerateMerchant(t).getClass(), Merchant.class);
        assertEquals(1, merchantMap.getGeneratedMerchants().size());
        assertEquals(1, locationMap.getGeneratedLocations().size());
        assertEquals(1, stateMap.getGeneratedStates().size());
        stateMap.getGeneratedStates().remove(t.getMerchant_state());
        locationMap.getGeneratedLocations().remove(t.getMerchant_zip());
        merchantMap.getGeneratedMerchants().remove(t.getMerchant_name());
        assertTrue(merchantMap.getGeneratedMerchants().isEmpty());
        assertTrue(locationMap.getGeneratedLocations().isEmpty());
        assertTrue(stateMap.getGeneratedStates().isEmpty());
    }
}
