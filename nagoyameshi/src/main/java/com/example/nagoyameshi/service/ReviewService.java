package com.example.nagoyameshi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> findByStoreId(Integer id) {
        return reviewRepository.findByStoreInfoId(id);
    }

    @Transactional
    public void save(Review review) {
        reviewRepository.save(review);
    }

    @Transactional
    public void updateReview(Integer reviewId, String comment, int star) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("レビューが見つかりません"));
        review.setComment(comment);
        review.setStar(star);
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Integer reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("レビューが見つかりません");
        }
        reviewRepository.deleteById(reviewId);
    }

    public Review findById(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("レビューが見つかりません"));
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
}