package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.cache.MerchantCache;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class MerchantProcessor implements ItemProcessor<Transaction, Object> {

    private static final MerchantCache merchantCache = MerchantCache.getInstance();

    @Override
    public Object process(Transaction item) throws Exception {
        merchantCache.findOrGenerateMerchant(item);
        return item;
    }
}
