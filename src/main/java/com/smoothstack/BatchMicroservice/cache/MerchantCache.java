package com.smoothstack.BatchMicroservice.cache;

import com.smoothstack.BatchMicroservice.generator.MerchantGenerator;
import com.smoothstack.BatchMicroservice.model.Merchant;
import com.smoothstack.BatchMicroservice.model.Transaction;

import java.util.*;

public class MerchantCache {
    private final HashMap<String, Merchant> generatedMerchants = new HashMap<>();
    private final Map<String, Merchant> syncGeneratedMerchants = Collections.synchronizedMap(generatedMerchants);
    private final Set<String> seenMerchants = new HashSet<>();
    private final Set<String> syncSeenMerchants = Collections.synchronizedSet(seenMerchants);
    private static MerchantCache merchantCacheInstance = null;

    public static MerchantCache getInstance(){
        if(merchantCacheInstance==null) merchantCacheInstance = new MerchantCache();
        return merchantCacheInstance;
    }

    public synchronized Merchant findOrGenerateMerchant(Transaction item){
        MerchantGenerator merchantGenerator = MerchantGenerator.getInstance();
        if(getGeneratedMerchant(item.getMerchant_name()) == null){
            synchronized (MerchantGenerator.class){
                if(getGeneratedMerchant(item.getMerchant_name()) == null){
                    merchantGenerator.generateMerchant(item, this);
                }
            }
        }
        return getGeneratedMerchant(item.getMerchant_name());
    }

    private Merchant getGeneratedMerchant(String merchant_name) {
        return syncGeneratedMerchants.get(merchant_name);
    }

    public void addGeneratedMerchant(String merchant_name, Merchant merchant){
        syncGeneratedMerchants.put(merchant_name, merchant);
    }

    public Map<String, Merchant> getGeneratedMerchants() {
        return syncGeneratedMerchants;
    }

    public Set<String> getSeenMerchants() {
        return syncSeenMerchants;
    }

    public void setSeenMerchant(String k) {
        syncSeenMerchants.add(k);
    }
}
