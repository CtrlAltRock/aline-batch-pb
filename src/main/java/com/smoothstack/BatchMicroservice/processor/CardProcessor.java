package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.cache.CardCache;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class CardProcessor implements ItemProcessor<Transaction, Object> {
    private final CardCache cardCache = CardCache.getInstance();

    @Override
    public Transaction process(Transaction item) {
        cardCache.findOrGenerateCard(item.getUser(), item.getCard());
        return item;
    }
}
