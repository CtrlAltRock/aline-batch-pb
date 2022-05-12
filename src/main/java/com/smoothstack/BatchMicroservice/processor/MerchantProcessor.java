package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.maps.MerchantMap;
import com.smoothstack.BatchMicroservice.model.generation.Merchant;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class MerchantProcessor implements ItemProcessor<Transaction, Object> {

    private static final MerchantMap MERCHANT_MAP = MerchantMap.getInstance();

    @Override
    public Object process(Transaction item) throws Exception {
        Merchant merchant = MERCHANT_MAP.findOrGenerateMerchant(item);
        MERCHANT_MAP.setRecurringTransaction(merchant.getId(), item.getAmount());
        return item;
    }
}
