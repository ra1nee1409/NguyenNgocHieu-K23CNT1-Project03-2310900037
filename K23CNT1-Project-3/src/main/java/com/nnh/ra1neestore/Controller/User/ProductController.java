package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Entity.Product;

import com.nnh.ra1neestore.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller xử lý hiển thị danh sách sản phẩm cho người dùng (Public).
 * Đường dẫn gốc: /products
 */
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final com.nnh.ra1neestore.Repository.ReviewRepository reviewRepository;
    private final com.nnh.ra1neestore.Repository.UserRepository userRepository;

    /**
     * Hiển thị danh sách tất cả sản phẩm (Public)
     */
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "user/products/list";
    }

    /**
     * Hiển thị chi tiết sản phẩm
     */
    @GetMapping("/{id}")
    public String productDetail(@org.springframework.web.bind.annotation.PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Lấy danh sách review
        List<com.nnh.ra1neestore.Entity.Review> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(id);

        // Lấy sản phẩm liên quan
        List<Product> relatedProducts = productService.getRelatedProducts(id, product.getCategory().getId());

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("relatedProducts", relatedProducts);

        // Thêm object review mới để form binding
        model.addAttribute("newReview", new com.nnh.ra1neestore.Entity.Review());

        return "user/products/detail";
    }

    /**
     * Xử lý thêm review
     */
    @org.springframework.web.bind.annotation.PostMapping("/{productId}/review")
    public String addReview(@org.springframework.web.bind.annotation.PathVariable("productId") Long productId,
            @org.springframework.web.bind.annotation.ModelAttribute("newReview") com.nnh.ra1neestore.Entity.Review review,
            java.security.Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        com.nnh.ra1neestore.Entity.User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Critical fix: Prevent ID collision from path variable
        review.setId(null);

        review.setProduct(product);
        review.setUser(user);
        review.setCreatedAt(java.time.LocalDateTime.now());

        reviewRepository.save(review);

        return "redirect:/products/" + productId;
    }
}
