package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.maps.MerchantMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.analysis.Top5RecurringTransaction;
import org.springframework.batch.item.ItemProcessor;

public class MerchantProcessor implements ItemProcessor<Transaction, Object> {

    private static final MerchantMap MERCHANT_MAP = MerchantMap.getInstance();

    @Override
    public Object process(Transaction item) throws Exception {
        MERCHANT_MAP.findOrGenerateMerchant(item);
        Top5RecurringTransaction t5re = new Top5RecurringTransaction(item);
        MERCHANT_MAP.setRecurringTransaction(item.getMerchant_name(), t5re.toString());
        return item;
    }
}
