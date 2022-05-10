package com.smoothstack.BatchMicroservice.maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TransactionMap {

    private static final class TransactionMapInstanceHolder {
        static final TransactionMap transactionMapInstance = new TransactionMap();
    }

    public static TransactionMap getInstance() {
        return TransactionMapInstanceHolder.transactionMapInstance;
    }

    private final HashMap<Integer, Integer> transactionByYear = new HashMap<>();
    private final Map<Integer, Integer> syncTransactionByYear = Collections.synchronizedMap(transactionByYear);
    private final HashMap<Integer, Integer> fraudByYear = new HashMap<>();
    private final Map<Integer, Integer> syncFraudByYear = Collections.synchronizedMap(fraudByYear);

    public Map<Integer, Integer> getSyncTransactionByYear() {
        return syncTransactionByYear;
    }

    public Map<Integer, Integer> getSyncFraudByYear() {
        return syncFraudByYear;
    }

    public void setTransactionByYear(Integer year){
        synchronized (this){
            if(!syncTransactionByYear.containsKey(year)) {
                syncTransactionByYear.put(year, 1);
            } else {
                syncTransactionByYear.replace(year, syncTransactionByYear.get(year)+1);
            }
        }
    }

    public void setFraudByYear(Integer year){
        synchronized (this){
            if(!syncFraudByYear.containsKey(year)){
                syncFraudByYear.put(year, 1);
            } else {
                syncFraudByYear.replace(year, syncFraudByYear.get(year)+1);
            }
        }
    }
}
