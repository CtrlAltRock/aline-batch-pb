package com.smoothstack.BatchMicroservice.generator;

import com.github.javafaker.Faker;
import com.smoothstack.BatchMicroservice.maps.LocationMap;
import com.smoothstack.BatchMicroservice.maps.MerchantMap;
import com.smoothstack.BatchMicroservice.model.generation.Merchant;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MerchantGenerator {
    private final Faker faker = new Faker();
    private Long incrementId = 0L;
    private final LocationMap locationMap = LocationMap.getInstance();

    private static final class MerchantGeneratorInstanceHolder {
        static final MerchantGenerator merchantGeneratorInstance = new MerchantGenerator();
    }

    public static MerchantGenerator getInstance() {
        return MerchantGeneratorInstanceHolder.merchantGeneratorInstance;
    }

    public synchronized Merchant generateMerchant(Transaction item, MerchantMap mc){
        Merchant merchant = new Merchant();
        merchant.setId(item.getMerchant_name());
        merchant.setUniqueKey(incrementId);
        incrementId+=1;
        merchant.setLocationId(locationMap.findOrGenerateLocation(item).getUniqueKey());
        merchant.setMcc(item.getMcc());
        merchant.setTransactions(new ArrayList<>());
        mc.addGeneratedMerchant(item.getMerchant_name(), merchant);
//        System.out.println(merchant);
        return merchant;
    }
}
