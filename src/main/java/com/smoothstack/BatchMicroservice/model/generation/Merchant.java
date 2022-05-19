package com.smoothstack.BatchMicroservice.model.generation;

import com.smoothstack.BatchMicroservice.model.Transaction;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Merchant {

    private String id;
    private Long uniqueKey;
    private List<Transaction> transactions;
    private Long locationId;
    private String mcc;

    public void setTransaction(Transaction item) {
        transactions.add(item);
    }
}
