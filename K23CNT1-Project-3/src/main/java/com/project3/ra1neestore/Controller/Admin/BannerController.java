package com.project3.ra1neestore.Controller.Admin;

import com.project3.ra1neestore.Entity.Banner;
import com.project3.ra1neestore.Service.BannerService;
import lombok.RequiredArgsConstructor;
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
 * Controller quản lý Banner cho Admin
 */
@Controller
@RequestMapping("/admin/banners")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BannerController {

    private final BannerService bannerService;
    private static final String UPLOAD_DIR = "src/main/resources/static/images/banners/";

    /**
     * Hiển thị danh sách banners
     */
    @GetMapping
    public String listBanners(Model model) {
        List<Banner> banners = bannerService.getAllBanners();
        model.addAttribute("banners", banners);
        return "admin/banners/list";
    }

    /**
     * Hiển thị form tạo banner mới
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Banner banner = new Banner();
        banner.setIsActive(true);
        banner.setDisplayOrder(0);
        model.addAttribute("banner", banner);
        model.addAttribute("isEdit", false);
        return "admin/banners/form";
    }

    /**
     * Xử lý tạo banner mới
     */
    @PostMapping
    public String createBanner(@ModelAttribute Banner banner,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Upload image if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = uploadBannerImage(imageFile);
                banner.setImageUrl(imageUrl);
            }

            bannerService.saveBanner(banner);
            redirectAttributes.addFlashAttribute("success", "Tạo banner thành công!");
            return "redirect:/admin/banners";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/banners/new";
        }
    }

    /**
     * Hiển thị form chỉnh sửa banner
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return bannerService.getBannerById(id)
                .map(banner -> {
                    model.addAttribute("banner", banner);
                    model.addAttribute("isEdit", true);
                    return "admin/banners/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy banner!");
                    return "redirect:/admin/banners";
                });
    }

    /**
     * Xử lý cập nhật banner
     */
    @PostMapping("/update/{id}")
    public String updateBanner(@PathVariable Long id,
            @ModelAttribute Banner banner,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Handle image upload
            bannerService.getBannerById(id).ifPresent(oldBanner -> {
                if (imageFile != null && !imageFile.isEmpty()) {
                    try {
                        // Delete old image
                        if (oldBanner.getImageUrl() != null) {
                            deleteBannerImage(oldBanner.getImageUrl());
                        }

                        // Upload new image
                        String imageUrl = uploadBannerImage(imageFile);
                        banner.setImageUrl(imageUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    banner.setImageUrl(oldBanner.getImageUrl());
                }
            });

            bannerService.saveBanner(banner);
            redirectAttributes.addFlashAttribute("success", "Cập nhật banner thành công!");
            return "redirect:/admin/banners";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/banners/edit/" + id;
        }
    }

    /**
     * Xử lý xóa banner
     */
    @PostMapping("/delete/{id}")
    public String deleteBanner(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bannerService.getBannerById(id).ifPresent(banner -> {
                if (banner.getImageUrl() != null) {
                    deleteBannerImage(banner.getImageUrl());
                }
            });
            bannerService.deleteBanner(id);
            redirectAttributes.addFlashAttribute("success", "Xóa banner thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/banners";
    }

    // ==========================================
    // IMAGE UPLOAD UTILS (Private)
    // ==========================================

    private String uploadBannerImage(MultipartFile file) throws IOException {
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

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/images/banners/" + uniqueFilename;
    }

    private void deleteBannerImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            String filePath = "src/main/resources/static" + imageUrl;
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            System.err.println("Không thể xóa ảnh: " + imageUrl);
        }
    }
}
