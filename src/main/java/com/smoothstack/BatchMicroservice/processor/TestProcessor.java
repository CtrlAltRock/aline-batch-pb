package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.generator.UserGenerator;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;


public class TestProcessor implements ItemProcessor<Transaction, Transaction> {

    private UserGenerator userGenerator = new UserGenerator();

    @Override
    public Transaction process(Transaction item) throws Exception {
        System.out.println(userGenerator.findUserOrGenerate(item.getUser()));
        return item;
    }
}
