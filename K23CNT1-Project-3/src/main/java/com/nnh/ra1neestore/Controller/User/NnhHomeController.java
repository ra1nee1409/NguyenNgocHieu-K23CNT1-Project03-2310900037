package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Config.CustomUserDetails;
import com.nnh.ra1neestore.Entity.NnhProduct;
import com.nnh.ra1neestore.Repository.NnhCategoryRepository;
import com.nnh.ra1neestore.Service.NnhProductService;
import com.nnh.ra1neestore.Service.NnhBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller xử lý trang chủ và các trang công khai.
 */
@Controller
@RequiredArgsConstructor
public class NnhHomeController {

    private final NnhProductService nnhProductService;
    private final NnhCategoryRepository nnhCategoryRepository;
    private final NnhBannerService nnhBannerService;

    /**
     * Redirect root URL to /home
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    /**
     * Hiển thị trang chủ với danh sách danh mục và sản phẩm.
     */
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            jakarta.servlet.http.HttpServletRequest request,
            Model model) {

        // Force session creation to ensure CSRF token works correctly
        request.getSession(true);

        // Lấy danh sách categories
        var categories = nnhCategoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        // Lấy danh sách banners
        var banners = nnhBannerService.getActiveBanners();
        model.addAttribute("banners", banners);

        // Lấy sản phẩm với pagination (16 sản phẩm/trang = 4x4)
        List<NnhProduct> allProducts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            allProducts = nnhProductService.searchActiveProducts(keyword);
        } else if (categoryId != null || (minPrice != null && maxPrice != null)) {
            allProducts = nnhProductService.filterProducts(categoryId, minPrice, maxPrice);
        } else {
            allProducts = nnhProductService.getActiveProducts();
        }

        // Pagination logic
        int pageSize = 16; // 4x4 grid
        int totalProducts = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalProducts);

        List<NnhProduct> nnhProducts = allProducts.subList(startIndex, endIndex);

        model.addAttribute("nnhProducts", nnhProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalProducts", totalProducts);

        // User info
        if (userDetails != null) {
            model.addAttribute("isGuest", false);
            model.addAttribute("nnhUser", userDetails.getNnhUser());
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("fullName", userDetails.getFullName());
            model.addAttribute("isAdmin", userDetails.getRole().name().equals("ADMIN"));
        } else {
            model.addAttribute("isGuest", true);
            model.addAttribute("username", "Khách");
            model.addAttribute("fullName", "Khách vãng lai");
            model.addAttribute("isAdmin", false);
        }

        return "user/home";
    }

    /**
     * Hiển thị trang giới thiệu.
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "Giới thiệu");
        return "user/about";
    }
}
