package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.maps.UserMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.batch.item.ItemProcessor;


public class UserProcessor implements ItemProcessor<Transaction, Object> {

    private static final UserMap USER_MAP = UserMap.getInstance();
    public static final String INSUFFICIENT_BALANCE = "Insufficient Balance";

    @Override
    public Transaction process(Transaction item) {
        USER_MAP.findUserOrGenerate(item.getUser());

        // get errors mapped by userid
        if(!item.getErrors().isEmpty()){
            String[] split = item.getErrors().split(",");
            for (String s : split) {
                if (s.equals(INSUFFICIENT_BALANCE)) {
                    USER_MAP.setInsufficientBalanceByUser(item.getUser());
                }
            }
        }
        return item;
    }
}
