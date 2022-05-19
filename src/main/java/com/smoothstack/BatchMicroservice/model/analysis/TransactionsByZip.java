package com.smoothstack.BatchMicroservice.model.analysis;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TransactionsByZip {
    @XStreamAlias("zipcode")
    private String zipcode;
    @XStreamAlias("occurrences")
    private Integer occurrences;
}
