package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.maps.TransactionMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class AnalysisProcessor implements ItemProcessor<Transaction, Object> {

    private static final TransactionMap tMap = TransactionMap.getInstance();

    @Override
    public Object process(Transaction item) throws Exception {
        // set up number of transactions by year
        tMap.setTransactionByYear(item.getYear());
        // set up error by insufficient balance
        if(item.getFraud().equalsIgnoreCase("yes")){
            tMap.setFraudByYear(item.getYear());
        }
        return item;
    }
}
