package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Tìm tất cả sản phẩm đang active (is_active = true)
     */
    List<Product> findByIsActiveTrue();

    /**
     * Tìm sản phẩm theo trạng thái active
     */
    List<Product> findByIsActive(Boolean isActive);

    /**
     * Tìm sản phẩm active theo category
     */
    /**
     * Tìm sản phẩm active theo category
     */
    List<Product> findByIsActiveTrueAndCategoryId(Long categoryId);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByNameContainingIgnoreCaseAndCategoryId(String keyword, Long categoryId);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByCategoryIdAndPriceBetween(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice);
}
