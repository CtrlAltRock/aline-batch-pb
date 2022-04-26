package com.smoothstack.BatchMicroservice.cache;

import com.smoothstack.BatchMicroservice.generator.StateGenerator;
import com.smoothstack.BatchMicroservice.model.State;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StateCache {
    private final HashMap<String, State> generatedStates = new HashMap<>();
    private final Map<String, State> syncGeneratedStates = Collections.synchronizedMap(generatedStates);
    private final Set<String> seenStates = new HashSet<>();
    private final Set<String> syncSeenStates = Collections.synchronizedSet(seenStates);

    private static final class StateCacheInstanceHolder {
        static final StateCache stateCacheInstance = new StateCache();
    }

    public static StateCache getInstance(){
        return StateCacheInstanceHolder.stateCacheInstance;
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
    public Set<String> getSeenStates() {
        return syncSeenStates;
    }

    public void setSeenState(String state){
        syncSeenStates.add(state);
    }

    public State getGeneratedState(String id){
        return syncGeneratedStates.get(id);
    }
}
