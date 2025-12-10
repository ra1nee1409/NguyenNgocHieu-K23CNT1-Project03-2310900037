package com.nnh.ra1neestore.Controller.Admin;

import com.nnh.ra1neestore.Entity.NnhBanner;
import com.nnh.ra1neestore.Service.NnhBannerService;
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

@Controller
@RequestMapping("/admin/nnhBanners")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class NnhBannerController {

    private final NnhBannerService nnhBannerService;

    @GetMapping
    public String index(Model model) {
        var banners = nnhBannerService.getAllBanners();
        model.addAttribute("banners", banners);
        model.addAttribute("nnhBanner", new NnhBanner());
        return "admin/banners/index";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute NnhBanner nnhBanner,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Upload image
            if (!imageFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                String uploadDir = "src/main/resources/static/images/banners/";

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                nnhBanner.setImageUrl("/images/banners/" + fileName);
            }

            nnhBannerService.saveBanner(nnhBanner);
            redirectAttributes.addFlashAttribute("success", "Thêm banner thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
        }

        return "redirect:/admin/nnhBanners";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
            @ModelAttribute NnhBanner nnhBanner,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        try {
            var existingBanner = nnhBannerService.getBannerById(id)
                    .orElseThrow(() -> new RuntimeException("Banner không tồn tại"));

            // Update fields
            existingBanner.setName(nnhBanner.getName());
            existingBanner.setDisplayOrder(nnhBanner.getDisplayOrder());
            existingBanner.setIsActive(nnhBanner.getIsActive());

            // Upload new image if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                // Delete old image
                if (existingBanner.getImageUrl() != null) {
                    String oldImagePath = "src/main/resources/static" + existingBanner.getImageUrl();
                    Files.deleteIfExists(Paths.get(oldImagePath));
                }

                // Upload new image
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                String uploadDir = "src/main/resources/static/images/banners/";
                Path filePath = Paths.get(uploadDir).resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                existingBanner.setImageUrl("/images/banners/" + fileName);
            }

            nnhBannerService.saveBanner(existingBanner);
            redirectAttributes.addFlashAttribute("success", "Cập nhật banner thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nnhBanners";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var banner = nnhBannerService.getBannerById(id)
                    .orElseThrow(() -> new RuntimeException("Banner không tồn tại"));

            // Delete image file
            if (banner.getImageUrl() != null) {
                String imagePath = "src/main/resources/static" + banner.getImageUrl();
                Files.deleteIfExists(Paths.get(imagePath));
            }

            nnhBannerService.deleteBanner(id);
            redirectAttributes.addFlashAttribute("success", "Xóa banner thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nnhBanners";
    }
}
