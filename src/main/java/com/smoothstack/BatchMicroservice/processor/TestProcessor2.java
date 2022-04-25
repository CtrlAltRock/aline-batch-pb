package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.cache.CardCache;
import com.smoothstack.BatchMicroservice.cache.UserCache;
import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.User;
import org.springframework.batch.item.ItemProcessor;

public class TestProcessor2 implements ItemProcessor<Transaction, Object> {

    private static final UserCache userCache = UserCache.getInstance();

    private final CardCache cardCache = CardCache.getInstance();

    @Override
    public Transaction process(Transaction item) {
        User user = userCache.getGeneratedUser(item.getUser());
        cardCache.findOrGenerateCard(item.getUser(), item.getCard());
        return item;
    }
}
