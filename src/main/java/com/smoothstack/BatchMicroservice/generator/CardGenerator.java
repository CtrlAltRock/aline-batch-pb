package com.smoothstack.BatchMicroservice.generator;

import com.vangogiel.luhnalgorithms.LuhnAlgorithms;

public class CardGenerator {

    public String makeCard(){
        return Long.toString(LuhnAlgorithms.generateRandomLuhn(16));
    }
    
    public boolean validCard(String card){
        return LuhnAlgorithms.isValid(card);
    }
}
