package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    /**
     * Tìm tất cả items trong giỏ hàng của user
     */
    List<Cart> findByUserId(Long userId);
    
    /**
     * Tìm item cụ thể trong giỏ hàng
     */
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
    
    /**
     * Xóa tất cả items trong giỏ hàng của user
     */
    void deleteByUserId(Long userId);
    
    /**
     * Đếm số lượng items trong giỏ hàng
     */
    long countByUserId(Long userId);
}
