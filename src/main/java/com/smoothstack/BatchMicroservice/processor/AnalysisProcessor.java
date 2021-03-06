package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.maps.TransactionMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class AnalysisProcessor implements ItemProcessor<Transaction, Object> {

    private static final TransactionMap tMap = TransactionMap.getInstance();

    private static final String YES = "Yes";

    @Override
    public Object process(Transaction item) {
        // set up number of transactions by year
        tMap.setTransactionByYear(item.getYear());

        // set up number of fraud by year
        if (item.getFraud().equalsIgnoreCase(YES)) {
            tMap.setFraudByYear(item.getYear());
        }

        // transaction types
        tMap.setTransactionType(item.getMethod());

        // top 10 largest
        tMap.setTopTenLargest(item);

        // top 5 by zipcode
        if (!item.getMerchant_zip().isBlank()) {
            tMap.setZipcodeTransaction(item);
        }

        // greater than $100, after 8pm, by zip or online
        if(Float.parseFloat(item.getAmount().replace("$", "")) > 100){
            tMap.checkAndSet8pmOver100(item);
        }

        return item;
    }
}
