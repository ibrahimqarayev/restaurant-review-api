package com.ibrahim.restaurant.service.impl;

import com.ibrahim.restaurant.api.restaurant.RestaurantCreateRequest;
import com.ibrahim.restaurant.api.restaurant.RestaurantUpdateRequest;
import com.ibrahim.restaurant.entity.Address;
import com.ibrahim.restaurant.entity.GeoLocation;
import com.ibrahim.restaurant.entity.Photo;
import com.ibrahim.restaurant.entity.Restaurant;
import com.ibrahim.restaurant.exception.RestaurantNotFoundException;
import com.ibrahim.restaurant.repository.RestaurantRepository;
import com.ibrahim.restaurant.service.GeoLocationService;
import com.ibrahim.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final GeoLocationService geoLocationService;

    @Override
    public Restaurant createRestaurant(RestaurantCreateRequest request) {
        Address address = request.getAddress();
        GeoLocation geoLocation = geoLocationService.geoLocate(address);
        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        List<String> photoIds = request.getPhotoIds();
        List<Photo> photos = photoIds.stream().map(photoUrl ->
                Photo.builder()
                        .url(photoUrl)
                        .uploadDate(LocalDateTime.now())
                        .build()).toList();

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .cuisineType(request.getCuisineType())
                .contactInformation(request.getContactInformation())
                .address(address)
                .geoLocation(geoPoint)
                .operatingHours(request.getOperatingHours())
                .averageRating(0f)
                .photos(photos)
                .build();

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Page<Restaurant> searchRestaurants(
            String query, Float minRating, Float latitude,
            Float longitude, Float radius, Pageable pageable) {

        if (minRating != null && (query == null || query.isEmpty())) {
            return restaurantRepository
                    .findByAverageRatingGreaterThanEqual(minRating, pageable);
        }

        Float searchMinRating = minRating != null ? minRating : 0f;

        if (query != null && !query.trim().isEmpty()) {
            return restaurantRepository
                    .findByQueryAndMinRating(query, searchMinRating, pageable);
        }

        if (longitude != null && latitude != null && radius != null) {
            return restaurantRepository
                    .findByLocationNear(longitude, latitude, radius, pageable);
        }

        return restaurantRepository.findAll(pageable);
    }

    @Override
    public Restaurant getRestaurant(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId));
    }

    @Override
    public Restaurant updateRestaurant(String restaurantId, RestaurantUpdateRequest request) {
        Restaurant restaurant = getRestaurant(restaurantId);

        GeoLocation newGeoLocation = geoLocationService.geoLocate(
                request.getAddress()
        );
        GeoPoint newGeoPoint = new GeoPoint(newGeoLocation.getLatitude(), newGeoLocation.getLongitude());

        List<String> photoIds = request.getPhotoIds();
        List<Photo> photos = photoIds.stream().map(photoUrl -> Photo.builder()
                .url(photoUrl)
                .uploadDate(LocalDateTime.now())
                .build()).toList();

        restaurant.setName(request.getName());
        restaurant.setCuisineType(request.getCuisineType());
        restaurant.setContactInformation(request.getContactInformation());
        restaurant.setAddress(request.getAddress());
        restaurant.setGeoLocation(newGeoPoint);
        restaurant.setOperatingHours(request.getOperatingHours());
        restaurant.setPhotos(photos);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(String restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }

}
