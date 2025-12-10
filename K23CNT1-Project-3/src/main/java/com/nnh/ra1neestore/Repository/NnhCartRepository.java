package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.NnhCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NnhCartRepository extends JpaRepository<NnhCart, Long> {

    List<NnhCart> findByNnhUserId(Long userId);

    Optional<NnhCart> findByNnhUserIdAndNnhProductId(Long userId, Long productId);

    @Modifying
    @Transactional
    void deleteByNnhUserId(Long userId);

    long countByNnhUserId(Long userId);
}
