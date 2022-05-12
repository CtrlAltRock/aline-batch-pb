package com.smoothstack.BatchMicroservice.model.analysis;

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

    @XStreamAlias("occurrences")
    private HashMap<Float, Integer> top5recurring;
}
