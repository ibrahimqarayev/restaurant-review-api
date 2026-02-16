package com.ibrahim.restaurant.service;

import com.ibrahim.restaurant.entity.Address;
import com.ibrahim.restaurant.entity.GeoLocation;

public interface GeoLocationService {
    GeoLocation geoLocate(Address address);
}
