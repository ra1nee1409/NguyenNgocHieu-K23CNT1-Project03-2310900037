package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.Entity.NnhProduct;
import com.nnh.ra1neestore.Repository.NnhProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service xử lý business logic cho Product
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NnhProductService {

    private final NnhProductRepository nnhProductRepository;

    /**
     * Lấy tất cả sản phẩm đang hoạt động
     */
    public List<NnhProduct> getActiveProducts() {
        return nnhProductRepository.findByIsActiveTrue();
    }

    /**
     * Lấy sản phẩm theo ID
     */
    public Optional<NnhProduct> getProductById(Long id) {
        return nnhProductRepository.findById(id);
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa (tên)
     */
    public List<NnhProduct> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveProducts();
        }
        return nnhProductRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(keyword);
    }

    /**
     * Lọc sản phẩm theo danh mục và khoảng giá
     */
    public List<NnhProduct> filterProducts(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        // Nếu có cả category và price range
        if (categoryId != null && minPrice != null && maxPrice != null) {
            return nnhProductRepository.findByNnhCategoryIdAndPriceBetweenAndIsActiveTrue(
                categoryId, minPrice, maxPrice);
        }
        
        // Chỉ có category
        if (categoryId != null) {
            return nnhProductRepository.findByNnhCategoryIdAndIsActiveTrue(categoryId);
        }
        
        // Chỉ có price range
        if (minPrice != null && maxPrice != null) {
            return nnhProductRepository.findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice);
        }
        
        // Không có filter nào
        return getActiveProducts();
    }

    /**
     * Tạo sản phẩm mới
     */
    public NnhProduct createProduct(NnhProduct nnhProduct) {
        return nnhProductRepository.save(nnhProduct);
    }

    /**
     * Cập nhật sản phẩm
     */
    public NnhProduct updateProduct(Long id, NnhProduct nnhProduct) {
        return nnhProductRepository.findById(id)
            .map(existing -> {
                existing.setName(nnhProduct.getName());
                existing.setDescription(nnhProduct.getDescription());
                existing.setPrice(nnhProduct.getPrice());
                existing.setSalePrice(nnhProduct.getSalePrice());
                existing.setNnhCategory(nnhProduct.getNnhCategory());
                existing.setImageUrl(nnhProduct.getImageUrl());
                existing.setStockQuantity(nnhProduct.getStockQuantity());
                existing.setIsActive(nnhProduct.getIsActive());
                return nnhProductRepository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Xóa sản phẩm
     */
    public void deleteProduct(Long id) {
        nnhProductRepository.deleteById(id);
    }

    /**
     * Lấy tất cả sản phẩm (bao gồm inactive) - cho admin
     */
    public List<NnhProduct> getAllProducts() {
        return nnhProductRepository.findAll();
    }

    /**
     * Tìm kiếm sản phẩm cho admin (bao gồm inactive)
     */
    public List<NnhProduct> searchProducts(String keyword, Long categoryId) {
        if (keyword != null && !keyword.trim().isEmpty() && categoryId != null) {
            return nnhProductRepository.findByNameContainingIgnoreCaseAndNnhCategoryId(keyword, categoryId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            return nnhProductRepository.findByNameContainingIgnoreCase(keyword);
        } else if (categoryId != null) {
            return nnhProductRepository.findByNnhCategoryId(categoryId);
        }
        return getAllProducts();
    }
}