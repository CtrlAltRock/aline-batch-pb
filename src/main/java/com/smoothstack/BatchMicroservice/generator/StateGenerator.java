package com.smoothstack.BatchMicroservice.generator;

import com.smoothstack.BatchMicroservice.cache.StateCache;
import com.smoothstack.BatchMicroservice.model.State;


public class StateGenerator {
    private static StateGenerator stateGeneratorInstance = null;

    public static StateGenerator getInstance(){
        if(stateGeneratorInstance == null) stateGeneratorInstance = new StateGenerator();
        return stateGeneratorInstance;
    }

    private Long incrementId = 0L;

    public synchronized State generateLocation(String merchant_state, StateCache stateCache) {
        State state = new State();
        state.setId(merchant_state);
        state.setUniqueKey(incrementId);
        incrementId+=1;
        stateCache.addGeneratedState(state);
        System.out.println(state);
        return state;
    }

}
