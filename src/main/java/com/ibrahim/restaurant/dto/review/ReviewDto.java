package com.ibrahim.restaurant.dto.review;

import com.ibrahim.restaurant.dto.photo.PhotoDto;
import com.ibrahim.restaurant.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private String id;
    private String content;
    private Integer rating;
    private LocalDateTime datePosted;
    private LocalDateTime lastEdited;
    private List<PhotoDto> photos = new ArrayList<>();
    private UserDto writtenBy;
}
