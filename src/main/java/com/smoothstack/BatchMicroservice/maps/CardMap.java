package com.smoothstack.BatchMicroservice.maps;

import com.smoothstack.BatchMicroservice.generator.CardGenerator;
import com.smoothstack.BatchMicroservice.model.generation.Card;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CardMap {

    private final HashMap<Long, HashSet<Card>> generatedCards = new HashMap<>();
    private final Map<Long, HashSet<Card>> syncGeneratedCards = Collections.synchronizedMap(generatedCards);

    public void clearAll() {
        syncGeneratedCards.clear();
    }


    private static final class CardMapInstanceHolder {
        static final CardMap cardMapInstance = new CardMap();
    }

    public static CardMap getInstance(){
        return CardMapInstanceHolder.cardMapInstance;
    }

    public synchronized void addGeneratedCard(Long userId, Card card){
        HashSet<Card> cards = syncGeneratedCards.get(userId);
        if(cards == null) cards = new HashSet<>();
        cards.add(card);
        syncGeneratedCards.put(userId, cards);
    }

    public HashSet<Card> getGeneratedUserCards(Long userId){
        return syncGeneratedCards.get(userId);
    }

    public Map<Long, HashSet<Card>> getGeneratedCards(){
        return syncGeneratedCards;
    }

    public void findOrGenerateCard(Long userId, Long cardIndex){
        CardGenerator cardGenerator = CardGenerator.getInstance();
        if(getGeneratedUserCards(userId) == null) {
            synchronized (CardGenerator.class) {
                if (getGeneratedUserCards(userId) == null) cardGenerator.instantiateCard(userId, this);
            }
        }
        if(getGeneratedUserCards(userId).size() != cardIndex+1){
            synchronized (CardGenerator.class) {
                if(getGeneratedUserCards(userId).size() != cardIndex+1){
                    for(int i = getGeneratedUserCards(userId).size(); i<cardIndex+1; i++) {
                        cardGenerator.addGeneratedCard(userId, this);
                    }
                }
            }
        }
    }
}
