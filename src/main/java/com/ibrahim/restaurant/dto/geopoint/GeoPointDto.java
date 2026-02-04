package com.ibrahim.restaurant.dto.geopoint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoPointDto {

    private Double latitude;
    private Double longitude;
}
