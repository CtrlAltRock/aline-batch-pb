package com.smoothstack.BatchMicroservice.maps;

import com.smoothstack.BatchMicroservice.model.Transaction;

import java.util.*;

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
    private final Set<String> transactionType = new HashSet<>();
    private final Set<String> syncTransactionType = Collections.synchronizedSet(transactionType);
    private final List<Transaction> top10LargestTransaction = new ArrayList<>();
    private final List<Transaction> syncTop10LargestTransaction = Collections.synchronizedList(top10LargestTransaction);
    private final HashMap<String, Integer> zipCodeTransaction = new HashMap<>();
    private final Map<String, Integer> syncZipCodeTransaction = Collections.synchronizedMap(zipCodeTransaction);
    private final HashMap<String, Integer> cityTransaction = new HashMap<>();
    private final Map<String, Integer> syncCityTransaction = Collections.synchronizedMap(cityTransaction);
    private final HashMap<String, Integer> transactionByStateNoFraud = new HashMap<>();
    private final Map<String, Integer> syncTransactionByStateNoFraud = Collections.synchronizedMap(transactionByStateNoFraud);
    private final HashMap<String, Integer> over100AndAfter8pm = new HashMap<>();
    private final Map<String, Integer> syncOver100AndAfter8pm = Collections.synchronizedMap(over100AndAfter8pm);


    public Map<Integer, Integer> getSyncTransactionByYear() {
        return syncTransactionByYear;
    }

    public Map<Integer, Integer> getSyncFraudByYear() {
        return syncFraudByYear;
    }

    public void setTopTenLargest(Transaction item) {
        int i = 0;
        Transaction stored = item;
        Float to = parseValue(item);
        synchronized (syncTop10LargestTransaction) {
            if (syncTop10LargestTransaction.size() == 10) {
                Float against = parseValue(syncTop10LargestTransaction.get(9));
            }
            while (i < 10) {
                if (i == syncTop10LargestTransaction.size()) {
                    syncTop10LargestTransaction.add(stored);
                    break;
                }
                Float query = parseValue(syncTop10LargestTransaction.get(i));
                if (query < to) {
                    Transaction transfer = syncTop10LargestTransaction.get(i);
                    syncTop10LargestTransaction.set(i, stored);
                    to = parseValue(transfer);
                    stored = transfer;
                }
                i++;
            }
        }
    }

    public void setTransactionByYear(Integer year) {
        synchronized (syncTransactionByYear) {
            if (!syncTransactionByYear.containsKey(year)) {
                syncTransactionByYear.put(year, 1);
            } else {
                syncTransactionByYear.replace(year, syncTransactionByYear.get(year) + 1);
            }
        }
    }

    public void setTransactionByState(String merchant_state) {
        synchronized (syncTransactionByStateNoFraud) {
            if (!syncTransactionByStateNoFraud.containsKey(merchant_state)) {
                syncTransactionByStateNoFraud.put(merchant_state, 1);
            } else {
                syncTransactionByStateNoFraud.replace(merchant_state, syncTransactionByStateNoFraud.get(merchant_state) + 1);
            }
        }
    }

    public Float parseValue(Transaction input) {
        return Float.parseFloat(input.getAmount().replace("$", ""));
    }

    public void setFraudByYear(Integer year) {
        synchronized (syncFraudByYear) {
            if (!syncFraudByYear.containsKey(year)) {
                syncFraudByYear.put(year, 1);
            } else {
                syncFraudByYear.replace(year, syncFraudByYear.get(year) + 1);
            }
        }
    }

    public void setZipcodeTransaction(Transaction item) {
        synchronized (syncZipCodeTransaction) {
            if (!syncZipCodeTransaction.containsKey(item.getMerchant_zip())) {
                syncZipCodeTransaction.put(item.getMerchant_zip(), 1);
            } else {
                Integer amount = syncZipCodeTransaction.get(item.getMerchant_zip()) + 1;
                syncZipCodeTransaction.replace(item.getMerchant_zip(), amount);
            }
        }
    }

    public void setCityTransaction(Transaction item){
        synchronized (syncCityTransaction) {
            if(!syncCityTransaction.containsKey(item.getMerchant_city())) {
                syncCityTransaction.put(item.getMerchant_city(), 1);
            } else {
                Integer amount = syncCityTransaction.get(item.getMerchant_city()) + 1;
                syncCityTransaction.replace(item.getMerchant_city(), amount);
            }
        }
    }

    public void setTransactionType(String method) {
        syncTransactionType.add(method);
    }

    public void checkAndSet8pmOver100(Transaction item) {
        int i = Integer.parseInt(item.getTime().replace(":", ""));
        if(i>2000){
            synchronized (syncOver100AndAfter8pm){
                if(!syncOver100AndAfter8pm.containsKey(item.getMerchant_zip())){
                    syncOver100AndAfter8pm.put(item.getMerchant_zip(), 1);
                } else {
                    syncOver100AndAfter8pm.replace(item.getMerchant_zip(), syncOver100AndAfter8pm.get(item.getMerchant_zip())+1);
                }
            }
        }
    }

    public Map<String, Integer> getSyncOver100AndAfter8pm() {
        return syncOver100AndAfter8pm;
    }

    public Set<String> getTransactionType() {
        return syncTransactionType;
    }

    public List<Transaction> getSyncTop10LargestTransaction() {
        return syncTop10LargestTransaction;
    }

    public Map<String, Integer> getSyncZipCodeTransaction() {
        return syncZipCodeTransaction;
    }

    public Map<String, Integer> getSyncCityTransaction() {
        return syncCityTransaction;
    }

    public Map<String, Integer> getSyncTransactionByStateNoFraud() {
        return syncTransactionByStateNoFraud;
    }




    public void clearAll() {
        syncTransactionByYear.clear();
        syncFraudByYear.clear();
        syncTransactionType.clear();
        syncTop10LargestTransaction.clear();
        syncZipCodeTransaction.clear();
        syncCityTransaction.clear();
        syncTransactionByStateNoFraud.clear();
    }
}
