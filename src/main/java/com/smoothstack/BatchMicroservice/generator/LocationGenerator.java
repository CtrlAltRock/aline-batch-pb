package com.smoothstack.BatchMicroservice.generator;

import com.smoothstack.BatchMicroservice.cache.LocationCache;
import com.smoothstack.BatchMicroservice.cache.StateCache;
import com.smoothstack.BatchMicroservice.model.Location;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class LocationGenerator {

    private static final class LocationGeneratorInstanceHolder {
        static final LocationGenerator locationGeneratorInstance = new LocationGenerator();
    }

    public static LocationGenerator getInstance(){
        return LocationGeneratorInstanceHolder.locationGeneratorInstance;
    }

    private final StateCache stateCache = StateCache.getInstance();

    private Long incrementId = 0L;

    public synchronized Location generateLocation(Transaction t, LocationCache lc){
        Location location = new Location();
        location.setZipCode(t.getMerchant_zip());
        location.setCity(t.getMerchant_city());
        location.setStateId(stateCache.findOrGenerateState(t.getMerchant_state()).getUniqueKey());
        location.setUniqueKey(incrementId);
        incrementId += 1;
        lc.addGeneratedLocation(location);
        System.out.println(location);
        return location;
    }
}
