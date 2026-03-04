package com.ibrahim.restaurant.mapper;

import com.ibrahim.restaurant.api.review.ReviewCreateRequest;
import com.ibrahim.restaurant.api.review.ReviewUpdateRequest;
import com.ibrahim.restaurant.dto.review.ReviewCreateRequestDto;
import com.ibrahim.restaurant.dto.review.ReviewDto;
import com.ibrahim.restaurant.dto.review.ReviewUpdateRequestDto;
import com.ibrahim.restaurant.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    ReviewCreateRequest toReviewCreateRequest(ReviewCreateRequestDto dto);

    ReviewUpdateRequest toReviewUpdateRequest(ReviewUpdateRequestDto dto);

    ReviewDto toDto(Review review);
}
