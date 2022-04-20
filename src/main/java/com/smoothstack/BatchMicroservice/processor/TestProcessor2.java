package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class TestProcessor2 implements ItemProcessor<Transaction, Transaction> {

    private Set<Long> userID = new HashSet<>();

    @Override
    public Transaction process(Transaction item) throws Exception {
        if(!userID.contains(item.getUser())){
           // generateUser
            userID.add(item.getUser());
            System.out.println(userID.toString());
        }
        return null;
    }
}
