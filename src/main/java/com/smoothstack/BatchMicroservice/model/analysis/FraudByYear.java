package com.smoothstack.BatchMicroservice.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FraudByYear {
    private Integer year;
    private Float percent;
}
