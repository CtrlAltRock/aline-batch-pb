package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.cache.UserCache;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;


public class UserProcessor implements ItemProcessor<Transaction, Object> {

    private static final UserCache userCache = UserCache.getInstance();

    @Override
    public Transaction process(Transaction item) {
        userCache.findUserOrGenerate(item.getUser());
        return item;
    }
}
