package com.smoothstack.BatchMicroservice.generator;

import com.smoothstack.BatchMicroservice.maps.StateMap;
import com.smoothstack.BatchMicroservice.model.State;


public class StateGenerator {

    private static final class StateGeneratorInstanceHolder {
        static final StateGenerator stateGeneratorInstance = new StateGenerator();
    }

    public static StateGenerator getInstance(){
        return StateGeneratorInstanceHolder.stateGeneratorInstance;
    }

    private Long incrementId = 0L;

    public synchronized State generateLocation(String merchant_state, StateMap stateMap) {
        State state = new State();
        state.setId(merchant_state);
        state.setUniqueKey(incrementId);
        incrementId+=1;
        stateMap.addGeneratedState(state);
//        System.out.println(state);
        return state;
    }

}
