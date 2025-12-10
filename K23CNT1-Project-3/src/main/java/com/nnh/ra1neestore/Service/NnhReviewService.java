package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.Entity.NnhProduct;
import com.nnh.ra1neestore.Entity.NnhReview;
import com.nnh.ra1neestore.Entity.NnhUser;
import com.nnh.ra1neestore.Repository.NnhReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NnhReviewService {

    private final NnhReviewRepository nnhReviewRepository;

    @Transactional
    public NnhReview saveReview(NnhReview review) {
        return nnhReviewRepository.save(review);
    }

    public List<NnhReview> getReviewsByProductId(Long productId) {
        return nnhReviewRepository.findByNnhProductIdOrderByCreatedAtDesc(productId);
    }

    public Double getAverageRating(Long productId) {
        Double avg = nnhReviewRepository.findAverageRatingByProductId(productId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    public Long getReviewCount(Long productId) {
        return nnhReviewRepository.countByProductId(productId);
    }

    public List<NnhReview> getAllReviews() {
        return nnhReviewRepository.findAll();
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        nnhReviewRepository.deleteById(reviewId);
    }
}
