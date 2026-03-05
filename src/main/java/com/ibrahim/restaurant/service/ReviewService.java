package com.ibrahim.restaurant.service;

import com.ibrahim.restaurant.api.review.ReviewCreateRequest;
import com.ibrahim.restaurant.api.review.ReviewUpdateRequest;
import com.ibrahim.restaurant.entity.Review;
import com.ibrahim.restaurant.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    Review createReview(User author, String restaurantId, ReviewCreateRequest review);

    Page<Review> listReviews(String restaurantId, Pageable pageable);

    Review getReview(String restaurantId,String reviewId);

    Review updateReview(User author, String reviewId, String restaurantId, ReviewUpdateRequest review);

    void deleteReview(String restaurantId, String reviewId);
}
