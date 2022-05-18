package com.smoothstack.BatchMicroservice.model.analysis;

import com.smoothstack.BatchMicroservice.model.Transaction;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Top5RecurringTransaction {
    @XStreamAlias("merchant-id")
    private String merchantId;
    private String amount;
    private Long userId;
    private Long cardIndex;

    private HashMap<Transaction, Integer> occurrences;


    public Top5RecurringTransaction(Transaction item){
        this.merchantId = item.getMerchant_name();
        this.amount = item.getAmount();
        this.userId = item.getUser();
        this.cardIndex = item.getCard();
    }

    @Override
    public String toString() {
        return "amount: " + amount +
                " userId: " + userId +
                " cardIndex: " + cardIndex;
    }
}
