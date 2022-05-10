package com.smoothstack.BatchMicroservice.model;

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
    private String name;
    private Long locationId;
    private String mcc;

    public void setTransaction(Transaction item) {
        transactions.add(item);
    }
}
