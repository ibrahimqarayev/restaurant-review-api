package com.ibrahim.restaurant.dto.restaurant;

import com.ibrahim.restaurant.dto.address.AddressDto;
import com.ibrahim.restaurant.dto.geopoint.GeoPointDto;
import com.ibrahim.restaurant.dto.operatinghours.OperatingHoursDto;
import com.ibrahim.restaurant.dto.photo.PhotoDto;
import com.ibrahim.restaurant.dto.review.ReviewDto;
import com.ibrahim.restaurant.dto.user.UserDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {

    private String id;
    private String name;
    private String cuisineType;
    private String contactInformation;
    private Float averageRatings;
    private GeoPointDto geoLocation;
    private AddressDto address;
    private OperatingHoursDto operatingHours;
    private List<PhotoDto> photos = new ArrayList<>();
    private List<ReviewDto> reviews = new ArrayList<>();
    private UserDto createdBy;
}
