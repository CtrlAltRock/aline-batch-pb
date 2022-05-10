package com.smoothstack.BatchMicroservice.maps;

import com.smoothstack.BatchMicroservice.generator.StateGenerator;
import com.smoothstack.BatchMicroservice.model.State;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StateMap {
    private final HashMap<String, State> generatedStates = new HashMap<>();
    private final Map<String, State> syncGeneratedStates = Collections.synchronizedMap(generatedStates);

    private static final class StateCacheInstanceHolder {
        static final StateMap STATE_MAP_INSTANCE = new StateMap();
    }

    public static StateMap getInstance(){
        return StateCacheInstanceHolder.STATE_MAP_INSTANCE;
    }

    public synchronized State findOrGenerateState(String merchant_state) {
        StateGenerator stateGenerator = StateGenerator.getInstance();
        if(!syncGeneratedStates.containsKey(merchant_state)){
            synchronized (StateGenerator.class){
                if(!syncGeneratedStates.containsKey(merchant_state)){
                    stateGenerator.generateLocation(merchant_state, this);
                }
            }
        }
        return getGeneratedState(merchant_state);
    }

    public Map<String, State> getGeneratedStates() {
        return syncGeneratedStates;
    }

    public void addGeneratedState(State state){
        syncGeneratedStates.put(state.getId(), state);
    }

    public State getGeneratedState(String id){
        return syncGeneratedStates.get(id);
    }
}
