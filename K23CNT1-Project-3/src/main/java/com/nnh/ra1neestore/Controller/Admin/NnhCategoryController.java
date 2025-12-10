package com.nnh.ra1neestore.Controller.Admin;

import com.nnh.ra1neestore.Entity.NnhCategory;
import com.nnh.ra1neestore.Service.NnhCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller xử lý quản lý danh mục
 */
@Slf4j
@Controller
@RequestMapping("/admin/nnhCategories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class NnhCategoryController {

    private final NnhCategoryService nnhCategoryService;

    /**
     * Danh sách danh mục (kết hợp thêm/sửa trong cùng 1 trang)
     */
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", nnhCategoryService.getAllCategories());
        model.addAttribute("nnhCategory", new NnhCategory());
        return "admin/categories/index";
    }

    /**
     * Thêm danh mục mới
     */
    @PostMapping("/add")
    public String addCategory(@ModelAttribute NnhCategory nnhCategory, RedirectAttributes redirectAttributes) {
        try {
            nnhCategoryService.createCategory(nnhCategory);
            redirectAttributes.addFlashAttribute("success", "Thêm danh mục thành công!");
        } catch (Exception e) {
            log.error("Error adding category", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/nnhCategories";
    }

    /**
     * Cập nhật danh mục
     */
    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute NnhCategory nnhCategory,
            RedirectAttributes redirectAttributes) {
        try {
            nnhCategoryService.updateCategory(id, nnhCategory);
            redirectAttributes.addFlashAttribute("success", "Cập nhật danh mục thành công!");
        } catch (Exception e) {
            log.error("Error updating category", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/nnhCategories";
    }

    /**
     * Xóa danh mục
     */
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            nnhCategoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công!");
        } catch (Exception e) {
            log.error("Error deleting category", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/nnhCategories";
    }
}
