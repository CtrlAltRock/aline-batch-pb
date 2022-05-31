package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.maps.CardMap;
import com.smoothstack.BatchMicroservice.maps.TransactionMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class CardProcessor implements ItemProcessor<Transaction, Object> {
    private static final CardMap cardMap = CardMap.getInstance();

    private static final TransactionMap tMap = TransactionMap.getInstance();

    private static final String ONLINE = "ONLINE";
    private static final String YES = "Yes";

    @Override
    public Transaction process(Transaction item) {
        cardMap.findOrGenerateCard(item.getUser(), item.getCard());

        //top 5 per city
        if (!item.getMerchant_city().trim().equalsIgnoreCase(ONLINE)) {
            tMap.setCityTransaction(item);
        }

        //no fraud by state
        if(!item.getFraud().equalsIgnoreCase(YES) && !item.getMerchant_state().isBlank()){
            tMap.setTransactionByState(item.getMerchant_state());
        }

        return item;
    }

}
