package com.ibrahim.restaurant.controller;

import com.ibrahim.restaurant.api.review.ReviewCreateRequest;
import com.ibrahim.restaurant.api.review.ReviewUpdateRequest;
import com.ibrahim.restaurant.dto.review.ReviewCreateRequestDto;
import com.ibrahim.restaurant.dto.review.ReviewDto;
import com.ibrahim.restaurant.dto.review.ReviewUpdateRequestDto;
import com.ibrahim.restaurant.entity.Review;
import com.ibrahim.restaurant.entity.User;
import com.ibrahim.restaurant.mapper.ReviewMapper;
import com.ibrahim.restaurant.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable String restaurantId,
            @RequestBody ReviewCreateRequestDto createRequestDto,
            @AuthenticationPrincipal Jwt jwt
    ) {

        ReviewCreateRequest reviewCreateRequest = reviewMapper.toReviewCreateRequest(createRequestDto);

        User user = jwtToUser(jwt);

        Review createdReview = reviewService.createReview(user, restaurantId, reviewCreateRequest);

        ReviewDto reviewDto = reviewMapper.toDto(createdReview);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reviewDto);
    }

    @GetMapping
    public Page<ReviewDto> listReviews(
            @PathVariable String restaurantId,
            @PageableDefault(
                    size = 20,
                    page = 0,
                    sort = "datePosted",
                    direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return reviewService
                .listReviews(restaurantId, pageable)
                .map(reviewMapper::toDto);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId
    ) {
        Review review = reviewService.getReview(restaurantId, reviewId);
        ReviewDto reviewDto = reviewMapper.toDto(review);

        return ResponseEntity.ok(reviewDto);
    }


    @PutMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt
    ) {
        ReviewUpdateRequest reviewCreateUpdateRequest = reviewMapper.toReviewUpdateRequest(review);
        User user = jwtToUser(jwt);

        Review updatedReview = reviewService.updateReview(
                user, restaurantId, reviewId, reviewCreateUpdateRequest
        );

        return ResponseEntity.ok(reviewMapper.toDto(updatedReview));
    }

    @DeleteMapping(path = "/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId
    ) {
        reviewService.deleteReview(restaurantId, reviewId);
        return ResponseEntity.noContent().build();
    }

    private User jwtToUser(Jwt jwt) {
        return User.builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .build();
    }
}
