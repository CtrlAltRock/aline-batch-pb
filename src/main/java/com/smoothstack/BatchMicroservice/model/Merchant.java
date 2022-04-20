package com.smoothstack.BatchMicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
public class Merchant {

//    @Id
    private Long id;

//    @OneToMany
    private List<Transaction> transactions;

    private String name;
    private String city;
    private String state;
    private String zip;
}
