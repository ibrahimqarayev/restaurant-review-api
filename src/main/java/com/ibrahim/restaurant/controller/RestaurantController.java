package com.ibrahim.restaurant.controller;

import com.ibrahim.restaurant.api.restaurant.RestaurantCreateRequest;
import com.ibrahim.restaurant.api.restaurant.RestaurantUpdateRequest;
import com.ibrahim.restaurant.dto.restaurant.RestaurantCreateRequestDto;
import com.ibrahim.restaurant.dto.restaurant.RestaurantDto;
import com.ibrahim.restaurant.dto.restaurant.RestaurantSummaryDto;
import com.ibrahim.restaurant.dto.restaurant.RestaurantUpdateRequestDto;
import com.ibrahim.restaurant.entity.Restaurant;
import com.ibrahim.restaurant.mapper.RestaurantMapper;
import com.ibrahim.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(
            @Valid @RequestBody RestaurantCreateRequestDto request) {

        RestaurantCreateRequest restaurantCreateRequest = restaurantMapper
                .toRestaurantCreateRequest(request);

        Restaurant restaurant = restaurantService.createRestaurant(restaurantCreateRequest);

        RestaurantDto createdRestaurantDto = restaurantMapper.toRestaurantDto(restaurant);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRestaurantDto);
    }

    @GetMapping
    public Page<RestaurantSummaryDto> searchRestaurants(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Float latitude,
            @RequestParam(required = false) Float longitude,
            @RequestParam(required = false) Float radius,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Page<Restaurant> searchResults = restaurantService.searchRestaurants(
                q, minRating, latitude, longitude, radius, PageRequest.of(page - 1, size)
        );

        return searchResults.map(restaurantMapper::toSummaryDto);
    }

    @GetMapping("/{restaurant_id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable("restaurant_id") String restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        RestaurantDto restaurantDto = restaurantMapper.toRestaurantDto(restaurant);
        return ResponseEntity.ok(restaurantDto);
    }


    @PutMapping(path = "/{restaurant_id}")
    public ResponseEntity<RestaurantDto> updateRestaurant(
            @PathVariable("restaurant_id") String restaurantId,
            @Valid @RequestBody RestaurantUpdateRequestDto requestDto
    ) {
        RestaurantUpdateRequest request = restaurantMapper
                .toRestaurantUpdateRequest(requestDto);

        Restaurant updatedRestaurant = restaurantService.updateRestaurant(restaurantId, request);

        return ResponseEntity.ok(restaurantMapper.toRestaurantDto(updatedRestaurant));
    }

    @DeleteMapping(path = "/{restaurant_id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("restaurant_id") String restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }
}
