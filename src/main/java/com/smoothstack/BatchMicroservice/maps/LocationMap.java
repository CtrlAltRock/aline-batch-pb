package com.smoothstack.BatchMicroservice.maps;

import com.smoothstack.BatchMicroservice.generator.LocationGenerator;
import com.smoothstack.BatchMicroservice.model.generation.Location;
import com.smoothstack.BatchMicroservice.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LocationMap {
    private final HashMap<String, Location> generatedLocations = new HashMap<>();
    private final Map<String, Location> syncGeneratedLocations = Collections.synchronizedMap(generatedLocations);

    private static final class LocationMapInstanceHolder {
        static final LocationMap locationMapInstance = new LocationMap();
    }

    public static LocationMap getInstance(){
        return LocationMapInstanceHolder.locationMapInstance;
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
