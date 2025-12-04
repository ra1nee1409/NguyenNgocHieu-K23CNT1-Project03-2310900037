package com.nnh.ra1neestore.Controller.Admin;

import com.nnh.ra1neestore.Entity.Category;
import com.nnh.ra1neestore.Entity.Product;
import com.nnh.ra1neestore.Repository.CategoryRepository;
import com.nnh.ra1neestore.Repository.ProductRepository;
import com.nnh.ra1neestore.Repository.UserRepository;
import com.nnh.ra1neestore.Service.ProductService;
import com.nnh.ra1neestore.Service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.UUID;

/**
 * Controller quản lý chung cho Admin (Dashboard + Products).
 * Đường dẫn gốc: /admin
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final BannerService bannerService;

    // Thư mục gốc lưu ảnh - sử dụng đường dẫn tuyệt đối
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/images/products/";

    // ==========================================
    // DASHBOARD SECTION
    // ==========================================

    /**
     * Redirect từ /admin sang /admin/dashboard
     */
    @GetMapping
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }

    /**
     * Hiển thị trang Dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Thống kê
        long totalProducts = productRepository.count();
        long totalUsers = userRepository.count();
        long activeProducts = productRepository.findByIsActive(true).size();
        long totalBanners = bannerService.getAllBanners().size();
        long totalOrders = 0; // TODO: Implement when Order entity is ready

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeProducts", activeProducts);
        model.addAttribute("totalBanners", totalBanners);
        model.addAttribute("totalOrders", totalOrders);

        return "admin/dashboard";
    }

    // ==========================================
    // PRODUCT MANAGEMENT SECTION
    // ==========================================

    /**
     * Hiển thị danh sách sản phẩm
     */
    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Long categoryId,
                               Model model) {
        List<Product> products = productService.searchProducts(keyword, categoryId);
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        return "admin/products/index";
    }

    /**
     * Hiển thị form tạo sản phẩm mới
     */
    @GetMapping("/products/add")
    public String showCreateForm(Model model) {
        Product product = new Product();
        product.setIsActive(true);
        product.setStockQuantity(0);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("isEdit", false);
        return "admin/products/add";
    }

    /**
     * Xử lý tạo sản phẩm mới
     */
    @PostMapping("/products")
    public String createProduct(@ModelAttribute Product product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Load category from database
            if (product.getCategory() != null && product.getCategory().getId() != null) {
                categoryRepository.findById(product.getCategory().getId())
                        .ifPresent(product::setCategory);
            }

            // Upload image if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String categoryName = product.getCategory() != null ? product.getCategory().getName() : "uncategorized";
                String imageUrl = uploadProductImage(imageFile, categoryName);
                product.setImageUrl(imageUrl);
            }

            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo sản phẩm thành công!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/products/add";
        }
    }

    /**
     * Hiển thị form chỉnh sửa sản phẩm
     */
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.getProductById(id)
                .map(product -> {
                    List<Category> categories = categoryRepository.findAll();
                    model.addAttribute("product", product);
                    model.addAttribute("categories", categories);
                    model.addAttribute("isEdit", true);
                    return "admin/products/edit";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm!");
                    return "redirect:/admin/products";
                });
    }

    /**
     * Xử lý cập nhật sản phẩm
     */
    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id,
            @ModelAttribute Product product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Load category from database
            if (product.getCategory() != null && product.getCategory().getId() != null) {
                categoryRepository.findById(product.getCategory().getId())
                        .ifPresent(product::setCategory);
            }

            // Handle image upload
            productService.getProductById(id).ifPresent(oldProduct -> {
                if (imageFile != null && !imageFile.isEmpty()) {
                    try {
                        // Delete old image
                        if (oldProduct.getImageUrl() != null) {
                            deleteProductImage(oldProduct.getImageUrl());
                        }

                        // Upload new image
                        String categoryName = product.getCategory() != null ? product.getCategory().getName()
                                : "uncategorized";
                        String imageUrl = uploadProductImage(imageFile, categoryName);
                        product.setImageUrl(imageUrl);
                    } catch (Exception e) {
                        log.error("Error uploading product image: {}", e.getMessage(), e);
                    }
                } else {
                    product.setImageUrl(oldProduct.getImageUrl());
                }
            });

            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            log.error("Error updating product {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/products/edit/" + id;
        }
    }

    /**
     * Xử lý xóa sản phẩm
     */
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.getProductById(id).ifPresent(product -> {
                if (product.getImageUrl() != null) {
                    deleteProductImage(product.getImageUrl());
                }
            });
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    // ==========================================
    // IMAGE UPLOAD UTILS (Private)
    // ==========================================

    private String uploadProductImage(MultipartFile file, String categoryName) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File rỗng!");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("File không phải là ảnh!");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        String normalizedCategoryName = removeVietnameseAccents(categoryName.toLowerCase())
                .replaceAll("[^a-z0-9]", "");
        
        // Get file bytes once
        byte[] fileBytes = file.getBytes();
        
        // Save to BOTH source and target directories
        String[] basePaths = {
            System.getProperty("user.dir") + "/src/main/resources/static/images/products/",
            System.getProperty("user.dir") + "/target/classes/static/images/products/"
        };
        
        for (String basePath : basePaths) {
            String categoryFolder = basePath + normalizedCategoryName + "/";
            Path uploadPath = Paths.get(categoryFolder);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.write(filePath, fileBytes);
        }

        return "/images/products/" + normalizedCategoryName + "/" + uniqueFilename;
    }

    private void deleteProductImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            String filePath = System.getProperty("user.dir") + "/src/main/resources/static" + imageUrl;
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            System.err.println("Không thể xóa ảnh: " + imageUrl);
        }
    }

    private String removeVietnameseAccents(String str) {
        if (str == null) {
            return "";
        }
        str = str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str = str.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        str = str.replaceAll("[ìíịỉĩ]", "i");
        str = str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str = str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str = str.replaceAll("[ỳýỵỷỹ]", "y");
        str = str.replaceAll("đ", "d");
        return str;
    }
}
