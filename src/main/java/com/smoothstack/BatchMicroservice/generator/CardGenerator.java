package com.smoothstack.BatchMicroservice.generator;

import com.smoothstack.BatchMicroservice.maps.CardMap;
import com.smoothstack.BatchMicroservice.model.generation.Card;
import com.vangogiel.luhnalgorithms.LuhnAlgorithms;

import java.util.HashSet;

public class CardGenerator {

    private Long incrementId = 0L;

    private static final class CardGeneratorInstanceHolder {
        static final CardGenerator cardGeneratorInstance = new CardGenerator();
    }

    public static CardGenerator getInstance(){
        return CardGeneratorInstanceHolder.cardGeneratorInstance;
    }
    public synchronized void instantiateCard(Long userId, CardMap cardMap) {
        cardMap.getGeneratedCards().put(userId, new HashSet<>());
    }

    public synchronized void addGeneratedCard(Long userId, CardMap cc){
        cc.addGeneratedCard(userId, makeCard(userId));
    }

    private synchronized Card makeCard(Long userId){
        Card card = new Card();
        card.setId(incrementId);
        card.setNumber(Long.toString(LuhnAlgorithms.generateRandomLuhn(16)));
        card.setUserId(userId);
        incrementId += 1;
//        System.out.println(card);
        return card;
    }

    private boolean validCard(String card){
        return LuhnAlgorithms.isValid(card);
    }

}
