package com.ibrahim.restaurant.dto.restaurant;

import com.ibrahim.restaurant.dto.address.AddressDto;
import com.ibrahim.restaurant.dto.photo.PhotoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSummaryDto {

    private String id;
    private String name;
    private String cuisineType;
    private Float averageRating;
    private Integer totalReviews;
    private AddressDto address;
    private List<PhotoDto> photos = new ArrayList<>();
}
