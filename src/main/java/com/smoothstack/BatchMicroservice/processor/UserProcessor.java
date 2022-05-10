package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.maps.UserMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;


public class UserProcessor implements ItemProcessor<Transaction, Object> {

    private static final UserMap USER_MAP = UserMap.getInstance();

    @Override
    public Transaction process(Transaction item) {
        USER_MAP.findUserOrGenerate(item.getUser());

        // get errors mapped by userid
        if(!item.getErrors().isEmpty()){
            String[] split = item.getErrors().split(",");
            for (String s : split) {
                if (s.equals("Insufficient Balance")) {
                    USER_MAP.setInsufficientBalanceByUser(item.getUser());
                }
            }
        }
        return item;
    }
}
