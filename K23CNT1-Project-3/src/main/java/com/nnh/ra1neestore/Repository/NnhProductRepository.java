package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.NnhProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface NnhProductRepository extends JpaRepository<NnhProduct, Long> {

    /**
     * Tìm tất cả sản phẩm đang active (is_active = true)
     */
    List<NnhProduct> findByIsActiveTrue();

    /**
     * Tìm sản phẩm theo trạng thái active
     */
    List<NnhProduct> findByIsActive(Boolean isActive);

    /**
     * Tìm sản phẩm active theo category
     */
    List<NnhProduct> findByNnhCategoryIdAndIsActiveTrue(Long categoryId);

    /**
     * Tìm sản phẩm theo category (bao gồm inactive)
     */
    List<NnhProduct> findByNnhCategoryId(Long categoryId);

    /**
     * Tìm kiếm sản phẩm theo tên (bao gồm inactive)
     */
    List<NnhProduct> findByNameContainingIgnoreCase(String keyword);

    /**
     * Tìm kiếm sản phẩm active theo tên
     */
    List<NnhProduct> findByNameContainingIgnoreCaseAndIsActiveTrue(String keyword);

    /**
     * Tìm kiếm sản phẩm theo tên và category (bao gồm inactive)
     */
    List<NnhProduct> findByNameContainingIgnoreCaseAndNnhCategoryId(String keyword, Long categoryId);

    /**
     * Tìm sản phẩm theo khoảng giá (bao gồm inactive)
     */
    List<NnhProduct> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Tìm sản phẩm active theo khoảng giá
     */
    List<NnhProduct> findByPriceBetweenAndIsActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Tìm sản phẩm theo category và khoảng giá (bao gồm inactive)
     */
    List<NnhProduct> findByNnhCategoryIdAndPriceBetween(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Tìm sản phẩm active theo category và khoảng giá
     */
    List<NnhProduct> findByNnhCategoryIdAndPriceBetweenAndIsActiveTrue(Long categoryId, BigDecimal minPrice,
            BigDecimal maxPrice);
}
