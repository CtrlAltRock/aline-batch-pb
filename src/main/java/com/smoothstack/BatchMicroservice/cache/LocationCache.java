package com.smoothstack.BatchMicroservice.cache;

import com.smoothstack.BatchMicroservice.generator.LocationGenerator;
import com.smoothstack.BatchMicroservice.model.Location;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LocationCache {
    private final HashMap<String, Location> generatedLocations = new HashMap<>();
    private final Map<String, Location> syncGeneratedLocations = Collections.synchronizedMap(generatedLocations);
    private final Set<String> seenLocations = new HashSet<>();
    private final Set<String> syncSeenLocations = Collections.synchronizedSet(seenLocations);

    private static LocationCache locationCacheInstance = null;

    public static LocationCache getInstance(){
        if(locationCacheInstance == null) locationCacheInstance = new LocationCache();
        return locationCacheInstance;
    }

    public Set<String> getSeenLocations(){
        return syncSeenLocations;
    }

    public void setSeenLocations(String zip){
        syncSeenLocations.add(zip);
    }

    public Map<String, Location> getGeneratedLocations() {
        return syncGeneratedLocations;
    }

    public void addGeneratedLocation(Location l){
        syncGeneratedLocations.put(l.getZipCode(), l);
    }

    public Location getGeneratedLocation(String zip){
        return syncGeneratedLocations.get(zip);
    }

    public Location findOrGenerateLocation(Transaction t){
        LocationGenerator locationGenerator = LocationGenerator.getInstance();
        if(!syncGeneratedLocations.containsKey(t.getMerchant_zip())){
            synchronized (LocationGenerator.class){
                if(!syncGeneratedLocations.containsKey(t.getMerchant_zip())){
                    locationGenerator.generateLocation(t, this);
                }
            }
        }
        return getGeneratedLocation(t.getMerchant_zip());
    }
}
