package com.smoothstack.BatchMicroservice.model.analysis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.DecimalFormat;

@Getter
@Setter
@ToString
public class InsufficientBalance {
    private Integer numberOfUsers;
    private Integer numberOfInsufficientUsers;
    private Float percentage;

    public InsufficientBalance(Integer numofUsers, Integer numOfInsUsers){
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        this.numberOfInsufficientUsers = numOfInsUsers;
        this.numberOfUsers = numofUsers;
        this.percentage = Float.valueOf(decimalFormat.format(((float)numOfInsUsers/(float)numofUsers) * 100f));
    }

}
