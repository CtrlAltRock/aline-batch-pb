package com.smoothstack.BatchMicroservice.maps;

import com.smoothstack.BatchMicroservice.generator.MerchantGenerator;
import com.smoothstack.BatchMicroservice.model.generation.Merchant;
import com.smoothstack.BatchMicroservice.model.Transaction;

import java.util.*;

public class MerchantMap {
    private final HashMap<String, Merchant> generatedMerchants = new HashMap<>();
    private final Map<String, Merchant> syncGeneratedMerchants = Collections.synchronizedMap(generatedMerchants);
    private final HashMap<String, HashMap<Float, Integer>> recurringTransaction = new HashMap<>();
    private final Map<String, HashMap<Float, Integer>> syncRecurringTransaction = Collections.synchronizedMap(recurringTransaction);
    private static final class MerchantMapInstanceHolder {
        static final MerchantMap merchantMapInstance = new MerchantMap();
    }

    public static MerchantMap getInstance(){
        return MerchantMapInstanceHolder.merchantMapInstance;
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

    public Integer getTotalUniqueMerchants(){
        return syncGeneratedMerchants.size();
    }
    public Map<String, HashMap<Float, Integer>> getRecurringTransaction() {
        return syncRecurringTransaction;
    }

    public void setRecurringTransaction(String id, String amount){
        synchronized (this){
            Float s = Float.valueOf(amount.split("\\$")[1]);
            if(!syncRecurringTransaction.containsKey(id)){
                syncRecurringTransaction.put(id, new HashMap<>());
            } else if (!syncRecurringTransaction.get(id).containsKey(s)) {
                syncRecurringTransaction.get(id).put(s, 1);
            } else {
                Integer count = syncRecurringTransaction.get(id).get(s) + 1;
                syncRecurringTransaction.get(id).put(s, count);
            }
        }
    }
}
