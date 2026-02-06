package com.ibrahim.restaurant.dto.photo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDto {

    private String url;
    private LocalDateTime uploadDate;
}
