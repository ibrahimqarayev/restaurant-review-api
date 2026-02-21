package com.ibrahim.restaurant.service;

import com.ibrahim.restaurant.api.restaurant.RestaurantCreateRequest;
import com.ibrahim.restaurant.api.restaurant.RestaurantUpdateRequest;
import com.ibrahim.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateRequest request);

    Page<Restaurant> searchRestaurants(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable
    );

    Restaurant getRestaurant(String restaurantId);

    Restaurant updateRestaurant(String restaurantId, RestaurantUpdateRequest restaurantUpdateRequest);

    void deleteRestaurant(String restaurantId);
}
