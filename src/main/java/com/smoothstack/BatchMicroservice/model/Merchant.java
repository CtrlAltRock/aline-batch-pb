package com.smoothstack.BatchMicroservice.model;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlRootElement(name="merchant")
@XmlAccessorType(XmlAccessType.FIELD)
public class Merchant {

    private String id;
    private Long uniqueKey;
    private List<Transaction> transactions;

    private String name;
    private String city;
    private String state;
    private String zip;
    private String mcc;
}
