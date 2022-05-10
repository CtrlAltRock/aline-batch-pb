package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.maps.CardMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class CardProcessor implements ItemProcessor<Transaction, Object> {
    private final CardMap cardMap = CardMap.getInstance();

    @Override
    public Transaction process(Transaction item) {
        cardMap.findOrGenerateCard(item.getUser(), item.getCard());
        return item;
    }
}
