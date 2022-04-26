package com.smoothstack.BatchMicroservice.cache;

import com.smoothstack.BatchMicroservice.generator.CardGenerator;
import com.smoothstack.BatchMicroservice.model.Card;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CardCache {

    private final HashMap<Long, HashSet<Card>> generatedCards = new HashMap<>();
    private final Map<Long, HashSet<Card>> syncGeneratedCards = Collections.synchronizedMap(generatedCards);
    private final HashSet<Long> seenCards = new HashSet<>();
    private final Set<Long> syncSeenUsers = Collections.synchronizedSet(seenCards);

    private static CardCache cardCacheInstance = null;

    public static CardCache getInstance(){
        if(cardCacheInstance == null) cardCacheInstance = new CardCache();
        return cardCacheInstance;
    }

    public Set<Long> getSeenCards(){
        return syncSeenUsers;
    }

    public void setSeenCards(Long id){
        syncSeenUsers.add(id);
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
