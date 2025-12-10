package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.NnhReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NnhReviewRepository extends JpaRepository<NnhReview, Long> {
    List<NnhReview> findByNnhProductIdOrderByCreatedAtDesc(Long productId);

    @Query("SELECT AVG(r.rating) FROM NnhReview r WHERE r.nnhProduct.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(r) FROM NnhReview r WHERE r.nnhProduct.id = :productId")
    Long countByProductId(@Param("productId") Long productId);
}
