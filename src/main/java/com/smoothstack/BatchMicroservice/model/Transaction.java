package com.smoothstack.BatchMicroservice.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private Long user;
    private Long card;
    private Integer year;
    private Integer month;
    private Integer day;
    private String time;
    private String amount;
    private String method;
    private String merchant_name;
    private String merchant_city;
    private String merchant_state;
    private String merchant_zip;
    private String mcc;
    private String errors;
    private String fraud;

}
