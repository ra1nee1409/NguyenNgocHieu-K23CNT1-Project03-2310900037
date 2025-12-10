package com.nnh.ra1neestore.Controller.Admin;

import com.nnh.ra1neestore.Entity.NnhProduct;
import com.nnh.ra1neestore.Repository.NnhBannerRepository;
import com.nnh.ra1neestore.Repository.NnhCategoryRepository;
import com.nnh.ra1neestore.Repository.NnhProductRepository;
import com.nnh.ra1neestore.Repository.NnhUserRepository;
import com.nnh.ra1neestore.Service.NnhProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Controller xử lý Admin Dashboard và Product Management
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class NnhAdminController {

    private final NnhProductRepository nnhProductRepository;
    private final NnhUserRepository nnhUserRepository;
    private final NnhBannerRepository nnhBannerRepository;
    private final NnhCategoryRepository nnhCategoryRepository;
    private final NnhProductService nnhProductService;

    @Value("${upload.path:src/main/resources/static/images/products}")
    private String uploadPath;

    /**
     * Dashboard - Trang tổng quan
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("Admin accessing dashboard");

        // Thống kê
        long totalProducts = nnhProductRepository.count();
        long totalUsers = nnhUserRepository.count();
        long totalBanners = nnhBannerRepository.count();
        long totalOrders = 0; // TODO: Implement when Order entity is ready

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalBanners", totalBanners);
        model.addAttribute("totalOrders", totalOrders);

        return "admin/dashboard";
    }

    // ==================== PRODUCT MANAGEMENT ====================

    /**
     * Danh sách sản phẩm
     */
    @GetMapping("/nnhProducts")
    public String listProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            Model model) {
        
        var products = nnhProductService.searchProducts(keyword, categoryId);
        var categories = nnhCategoryRepository.findAll();

        model.addAttribute("nnhProducts", products);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);

        return "admin/products/index";
    }

    /**
     * Form thêm sản phẩm mới
     */
    @GetMapping("/nnhProducts/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("nnhProduct", new NnhProduct());
        model.addAttribute("categories", nnhCategoryRepository.findAll());
        return "admin/products/add";
    }

    /**
     * Xử lý thêm sản phẩm
     */
    @PostMapping("/nnhProducts/add")
    public String addProduct(
            @ModelAttribute NnhProduct nnhProduct,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Upload ảnh nếu có
            if (!imageFile.isEmpty()) {
                String imageUrl = saveImage(imageFile, nnhProduct.getNnhCategory().getId());
                nnhProduct.setImageUrl(imageUrl);
            }

            nnhProductService.createProduct(nnhProduct);
            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
            
        } catch (Exception e) {
            log.error("Error adding product", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nnhProducts";
    }

    /**
     * Form sửa sản phẩm
     */
    @GetMapping("/nnhProducts/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return nnhProductService.getProductById(id)
            .map(product -> {
                model.addAttribute("nnhProduct", product);
                model.addAttribute("categories", nnhCategoryRepository.findAll());
                return "admin/products/edit";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
                return "redirect:/admin/nnhProducts";
            });
    }

    /**
     * Xử lý cập nhật sản phẩm
     */
    @PostMapping("/nnhProducts/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute NnhProduct nnhProduct,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        
        try {
            var existingProduct = nnhProductService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Upload ảnh mới nếu có
            if (!imageFile.isEmpty()) {
                // Xóa ảnh cũ
                if (existingProduct.getImageUrl() != null) {
                    deleteImage(existingProduct.getImageUrl());
                }
                
                String imageUrl = saveImage(imageFile, nnhProduct.getNnhCategory().getId());
                nnhProduct.setImageUrl(imageUrl);
            } else {
                // Giữ ảnh cũ
                nnhProduct.setImageUrl(existingProduct.getImageUrl());
            }

            nnhProductService.updateProduct(id, nnhProduct);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
            
        } catch (Exception e) {
            log.error("Error updating product", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nnhProducts";
    }

    /**
     * Xóa sản phẩm
     */
    @PostMapping("/nnhProducts/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var product = nnhProductService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Xóa ảnh
            if (product.getImageUrl() != null) {
                deleteImage(product.getImageUrl());
            }

            nnhProductService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
            
        } catch (Exception e) {
            log.error("Error deleting product", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nnhProducts";
    }

    // ==================== HELPER METHODS ====================

    /**
     * Lưu ảnh vào thư mục theo tên danh mục
     */
    private String saveImage(MultipartFile file, Long categoryId) throws IOException {
        // Lấy thông tin category
        var category = nnhCategoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        
        // Chuẩn hóa tên folder (loại bỏ dấu, chuyển thành chữ thường, thay khoảng trắng bằng -)
        String categoryFolderName = normalizeFileName(category.getName());
        
        // Tạo tên file unique
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        // Tạo đường dẫn theo tên category
        Path categoryPath = Paths.get(uploadPath, categoryFolderName);
        Files.createDirectories(categoryPath);

        // Lưu file
        Path filePath = categoryPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return URL
        return "/images/products/" + categoryFolderName + "/" + filename;
    }
    
    /**
     * Chuẩn hóa tên file/folder (loại bỏ dấu tiếng Việt, chuyển thành chữ thường)
     */
    private String normalizeFileName(String input) {
        if (input == null) return "";
        
        // Loại bỏ dấu tiếng Việt
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        
        // Chuyển thành chữ thường, thay khoảng trắng và ký tự đặc biệt bằng -
        normalized = normalized.toLowerCase()
                              .replaceAll("đ", "d")
                              .replaceAll("[^a-z0-9]+", "-")
                              .replaceAll("^-+|-+$", ""); // Loại bỏ - ở đầu/cuối
        
        return normalized;
    }

    /**
     * Xóa ảnh
     */
    private void deleteImage(String imageUrl) {
        try {
            if (imageUrl != null && imageUrl.startsWith("/images/products/")) {
                // Xóa file ảnh
                Path filePath = Paths.get("src/main/resources/static" + imageUrl);
                Files.deleteIfExists(filePath);
                log.info("Deleted image: {}", filePath);
            }
        } catch (IOException e) {
            log.error("Error deleting image: " + imageUrl, e);
        }
    }
}
