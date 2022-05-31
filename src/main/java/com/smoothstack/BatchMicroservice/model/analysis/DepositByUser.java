package com.smoothstack.BatchMicroservice.model.analysis;

import com.smoothstack.BatchMicroservice.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class DepositByUser {
    private Long user;
    private Set<Transaction> deposits;
}
