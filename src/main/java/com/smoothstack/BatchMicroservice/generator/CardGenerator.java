package com.smoothstack.BatchMicroservice.generator;

import com.smoothstack.BatchMicroservice.model.Card;
import com.smoothstack.BatchMicroservice.model.User;
import com.vangogiel.luhnalgorithms.LuhnAlgorithms;

import java.util.HashMap;

public class CardGenerator {

    private final HashMap<Long, Card> generatedCards = new HashMap<>();
    private static CardGenerator cardGeneratorInstance = null;

    private Long incrementId = 0L;

    public static CardGenerator getInstance(){
        if(cardGeneratorInstance == null) cardGeneratorInstance = new CardGenerator();
        return cardGeneratorInstance;
    }

    public synchronized Card makeCard(User user){
        incrementId = incrementId+1;
        Card card = new Card();
        card.setId(incrementId);
        card.setNumber(Long.toString(LuhnAlgorithms.generateRandomLuhn(16)));
        card.setUser(user);
        addGeneratedCard(user.getId(), card);
        System.out.println(card);
        return card;
    }

    private synchronized void addGeneratedCard(Long userId, Card card){
        generatedCards.put(userId, card);
    }
    
    public boolean validCard(String card){
        return LuhnAlgorithms.isValid(card);
    }
}
