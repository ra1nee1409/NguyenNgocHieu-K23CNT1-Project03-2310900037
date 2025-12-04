package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Config.CustomUserDetails;
import com.nnh.ra1neestore.Entity.Product;
import com.nnh.ra1neestore.Repository.CategoryRepository;
import com.nnh.ra1neestore.Service.ProductService;
import com.nnh.ra1neestore.Service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller xử lý trang chủ và các trang công khai.
 * Đường dẫn gốc: /
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final BannerService bannerService;

    /**
     * Redirect root URL to /home
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    /**
     * Hiển thị trang chủ với danh sách danh mục và sản phẩm.
     * Hỗ trợ lọc sản phẩm theo danh mục.
     *
     * @param userDetails Thông tin người dùng đang đăng nhập (nếu có)
     * @param categoryId  ID của danh mục cần lọc (tùy chọn)
     * @param model       Model để truyền dữ liệu sang view
     * @return Tên view template ("home")
     */
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            Model model) {
        // Lấy danh sách categories
        var categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        // Lấy danh sách banners
        var banners = bannerService.getActiveBanners();
        model.addAttribute("banners", banners);

        // Lấy sản phẩm theo logic: Search -> Filter (Category + Price) -> All Active
        List<Product> products;

        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchProducts(keyword);
        } else if (categoryId != null || (minPrice != null && maxPrice != null)) {
            products = productService.filterProducts(categoryId, minPrice, maxPrice);
        } else {
            products = productService.getActiveProducts();
        }

        model.addAttribute("products", products);

        // User info
        if (userDetails != null) {
            model.addAttribute("isGuest", false);
            model.addAttribute("user", userDetails.getUser());
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("fullName", userDetails.getFullName());
            model.addAttribute("isAdmin", userDetails.getRole().name().equals("admin"));
        } else {
            model.addAttribute("isGuest", true);
            model.addAttribute("username", "Khách");
            model.addAttribute("fullName", "Khách vãng lai");
            model.addAttribute("isAdmin", false);
        }

        return "user/home";
    }
}
