package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.Entity.Product;
import com.nnh.ra1neestore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        // Set thời gian tạo
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }

        // Set giá trị mặc định nếu null
        if (product.getIsActive() == null) {
            product.setIsActive(true);
        }
        if (product.getStockQuantity() == null) {
            product.setStockQuantity(0);
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        // Kiểm tra sản phẩm có tồn tại không
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        // Cập nhật thông tin
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setSalePrice(product.getSalePrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setIsActive(product.getIsActive());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        // Kiểm tra sản phẩm có tồn tại không
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sản phẩm với ID: " + id);
        }

        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Product> getRelatedProducts(Long productId, Long categoryId) {
        return productRepository.findByIsActiveTrueAndCategoryId(categoryId).stream()
                .filter(p -> !p.getId().equals(productId))
                .limit(4)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword, Long categoryId) {
        if (categoryId == null) {
            return searchProducts(keyword);
        }
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findByCategoryId(categoryId);
        }
        return productRepository.findByNameContainingIgnoreCaseAndCategoryId(keyword, categoryId);
    }

    @Transactional(readOnly = true)
    public List<Product> filterProducts(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        // Both category and price range
        if (categoryId != null && minPrice != null && maxPrice != null) {
            return productRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice);
        }
        // Only price range
        else if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        }
        // Only category
        else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId);
        }
        // No filters
        else {
            return getAllProducts();
        }
    }
}
