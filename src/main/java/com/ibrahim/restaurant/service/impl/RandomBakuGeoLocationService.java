package com.ibrahim.restaurant.service.impl;

import com.ibrahim.restaurant.entity.Address;
import com.ibrahim.restaurant.entity.GeoLocation;
import com.ibrahim.restaurant.service.GeoLocationService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomBakuGeoLocationService implements GeoLocationService {

    private static final float MIN_LATITUDE = 40.35f;
    private static final float MAX_LATITUDE = 40.55f;
    private static final float MIN_LONGITUDE = 49.80f;
    private static final float MAX_LONGITUDE = 50.05f;

    @Override
    public GeoLocation geoLocate(Address address) {
        Random random = new Random();
        double latitude = MIN_LATITUDE + random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE);
        double longitude = MIN_LONGITUDE + random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE);

        return GeoLocation.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
