package com.ibrahim.restaurant.service.impl;

import com.ibrahim.restaurant.api.review.ReviewCreateRequest;
import com.ibrahim.restaurant.api.review.ReviewUpdateRequest;
import com.ibrahim.restaurant.entity.Photo;
import com.ibrahim.restaurant.entity.Restaurant;
import com.ibrahim.restaurant.entity.Review;
import com.ibrahim.restaurant.entity.User;
import com.ibrahim.restaurant.exception.ReviewNotAllowedException;
import com.ibrahim.restaurant.exception.ReviewNotFoundException;
import com.ibrahim.restaurant.repository.RestaurantRepository;
import com.ibrahim.restaurant.service.RestaurantService;
import com.ibrahim.restaurant.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;

    @Override
    public Review createReview(User author, String restaurantId, ReviewCreateRequest review) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);

        boolean hasExistingReview = restaurant.getReviews().stream()
                .anyMatch(r -> r.getWrittenBy().getId().equals(author.getId()));

        if (hasExistingReview) {
            throw new ReviewNotAllowedException("User has already reviewed this restaurant");
        }

        LocalDateTime now = LocalDateTime.now();

        List<Photo> photos = review.getPhotoIds().stream()
                .map(url -> Photo.builder()
                        .url(url)
                        .uploadDate(now)
                        .build())
                .toList();

        String reviewId = UUID.randomUUID().toString();

        Review reviewToCreate = Review.builder()
                .id(reviewId)
                .content(review.getContent())
                .rating(review.getRating())
                .photos(photos)
                .datePosted(now)
                .lastEdited(now)
                .writtenBy(author)
                .build();

        restaurant.getReviews().add(reviewToCreate);

        updateRestaurantAverageRating(restaurant);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return savedRestaurant.getReviews()
                .stream()
                .filter(r -> r.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new ReviewNotFoundException("Error retrieving created review"));
    }

    @Override
    public Page<Review> listReviews(String restaurantId, Pageable pageable) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        List<Review> reviews = restaurant.getReviews();

        Sort sort = pageable.getSort();

        if (sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            boolean isAscending = order.getDirection().isAscending();

            Comparator<Review> comparator = switch (property) {
                case "datePosted" -> Comparator.comparing(Review::getDatePosted);
                case "rating" -> Comparator.comparing(Review::getRating);
                default -> Comparator.comparing(Review::getDatePosted);
            };

            reviews.sort(isAscending ? comparator : comparator.reversed());
        } else {
            reviews.sort(Comparator.comparing(Review::getDatePosted).reversed());
        }

        int start = (int) pageable.getOffset();

        if (start > reviews.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, reviews.size());
        }

        int end = Math.min((start + pageable.getPageSize()), reviews.size());

        return new PageImpl<>(reviews.subList(start, end), pageable, reviews.size());
    }

    @Override
    public Review getReview(String restaurantId, String reviewId) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        return getReviewFromRestaurant(reviewId, restaurant)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id:" + restaurantId));
    }


    @Override
    public Review updateReview(User author, String restaurantId, String reviewId, ReviewUpdateRequest review) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);

        String authorId = author.getId();
        Review existingReview = getReviewFromRestaurant(reviewId, restaurant)
                .orElseThrow(() -> new ReviewNotFoundException("Review does not exist"));

        if (!authorId.equals(existingReview.getWrittenBy().getId())) {
            throw new ReviewNotAllowedException("Cannot update another user's review");
        }

        if (LocalDateTime.now().isAfter(existingReview.getDatePosted().plusHours(48))) {
            throw new ReviewNotAllowedException("Review can no longer bew edited");
        }

        existingReview.setContent(review.getContent());
        existingReview.setRating(review.getRating());
        existingReview.setLastEdited(LocalDateTime.now());

        existingReview.setPhotos(review.getPhotoIds().stream()
                .map(photoId -> Photo.builder()
                        .url(photoId)
                        .uploadDate(LocalDateTime.now())
                        .build()).toList());

        List<Review> updatedReviews = restaurant.getReviews().stream()
                .filter(r -> !reviewId.equals(r.getId()))
                .collect(Collectors.toList());
        updatedReviews.add(existingReview);

        restaurant.setReviews(updatedReviews);

        updateRestaurantAverageRating(restaurant);

        restaurantRepository.save(restaurant);

        return existingReview;
    }

    @Override
    public void deleteReview(String restaurantId, String reviewId) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        List<Review> filteredReviews = restaurant.getReviews().stream()
                .filter(r -> !reviewId.equals(r.getId()))
                .toList();

        restaurant.setReviews(filteredReviews);

        updateRestaurantAverageRating(restaurant);

        restaurantRepository.save(restaurant);
    }

    private static Optional<Review> getReviewFromRestaurant(String reviewId, Restaurant restaurant) {
        return restaurant.getReviews()
                .stream()
                .filter(r -> reviewId.equals(r.getId()))
                .findFirst();
    }

    public void updateRestaurantAverageRating(Restaurant restaurant) {
        List<Review> reviews = restaurant.getReviews();

        if (reviews.isEmpty()) {
            restaurant.setAverageRating(0.0f);
        } else {
            double averageRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            restaurant.setAverageRating((float) averageRating);
        }
    }
}
