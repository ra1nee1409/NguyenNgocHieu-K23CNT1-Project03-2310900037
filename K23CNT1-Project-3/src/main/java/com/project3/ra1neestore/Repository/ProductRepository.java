package com.project3.ra1neestore.Repository;

import com.project3.ra1neestore.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
