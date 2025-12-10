package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Entity.NnhProduct;
import com.nnh.ra1neestore.Entity.NnhReview;
import com.nnh.ra1neestore.Entity.NnhUser;
import com.nnh.ra1neestore.Config.CustomUserDetails;
import com.nnh.ra1neestore.Service.NnhProductService;
import com.nnh.ra1neestore.Service.NnhReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class NnhProductController {

    private final NnhProductService nnhProductService;
    private final NnhReviewService nnhReviewService;

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        var product = nnhProductService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Get related products (same category, exclude current product)
        var relatedProducts = nnhProductService.getActiveProducts().stream()
                .filter(p -> p.getNnhCategory().getId().equals(product.getNnhCategory().getId()))
                .filter(p -> !p.getId().equals(id))
                .limit(4)
                .toList();

        // Get reviews for this product
        var reviews = nnhReviewService.getReviewsByProductId(id);
        var avgRating = nnhReviewService.getAverageRating(id);
        var reviewCount = nnhReviewService.getReviewCount(id);

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("reviewCount", reviewCount);

        return "user/product-detail";
    }

    @PostMapping("/{id}/review")
    public String submitReview(@PathVariable Long id,
            @RequestParam Integer rating,
            @RequestParam String comment,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            // Get product
            NnhProduct product = nnhProductService.getProductById(id)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Get current user
            NnhUser user = userDetails.getNnhUser();

            // Create review
            NnhReview review = NnhReview.builder()
                    .nnhProduct(product)
                    .nnhUser(user)
                    .rating(rating)
                    .comment(comment)
                    .build();

            nnhReviewService.saveReview(review);
            redirectAttributes.addFlashAttribute("successMessage", "Cảm ơn bạn đã đánh giá sản phẩm!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi gửi đánh giá: " + e.getMessage());
        }

        return "redirect:/product/" + id;
    }
}
