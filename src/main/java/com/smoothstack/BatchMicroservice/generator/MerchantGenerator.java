package com.smoothstack.BatchMicroservice.generator;

import com.github.javafaker.Faker;
import com.smoothstack.BatchMicroservice.cache.LocationCache;
import com.smoothstack.BatchMicroservice.cache.MerchantCache;
import com.smoothstack.BatchMicroservice.model.Merchant;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MerchantGenerator {
    private final Faker faker = new Faker();
    private Long incrementId = 0L;
    private final LocationCache locationCache = LocationCache.getInstance();
    private static MerchantGenerator merchantGeneratorInstance = null;

    public static MerchantGenerator getInstance() {
        if(merchantGeneratorInstance == null) merchantGeneratorInstance = new MerchantGenerator();
        return merchantGeneratorInstance;
    }

    public synchronized Merchant generateMerchant(Transaction item, MerchantCache mc){
        Merchant merchant = new Merchant();
        merchant.setId(item.getMerchant_name());
        merchant.setUniqueKey(incrementId);
        incrementId+=1;
        merchant.setName(faker.company().name());
        merchant.setLocationId(locationCache.findOrGenerateLocation(item).getUniqueKey());
        merchant.setMcc(item.getMcc());
        merchant.setTransactions(new ArrayList<>());
        mc.addGeneratedMerchant(item.getMerchant_name(), merchant);
        System.out.println(merchant);
        return merchant;
    }
}
