package com.nnh.ra1neestore.Controller.Admin;

import com.nnh.ra1neestore.Entity.Category;
import com.nnh.ra1neestore.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller quản lý Category cho Admin
 */
@Slf4j
@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Hiển thị danh sách categories
     */
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/categories/index";
    }

    /**
     * Hiển thị form tạo category mới
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        model.addAttribute("isEdit", false);
        return "admin/categories/form";
    }

    /**
     * Xử lý tạo category mới
     */
    @PostMapping
    public String createCategory(@ModelAttribute Category category,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Check if name already exists
            if (categoryService.existsByName(category.getName())) {
                redirectAttributes.addFlashAttribute("error", "Tên danh mục đã tồn tại!");
                return "redirect:/admin/categories/new";
            }

            categoryService.createCategory(category);
            redirectAttributes.addFlashAttribute("success", "Tạo danh mục thành công!");
            return "redirect:/admin/categories";
        } catch (Exception e) {
            log.error("Error creating category: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/categories/new";
        }
    }

    /**
     * Hiển thị form chỉnh sửa category
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return categoryService.getCategoryById(id)
                .map(category -> {
                    model.addAttribute("category", category);
                    model.addAttribute("isEdit", true);
                    return "admin/categories/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục!");
                    return "redirect:/admin/categories";
                });
    }

    /**
     * Xử lý cập nhật category
     */
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute Category category,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.updateCategory(id, category);
            redirectAttributes.addFlashAttribute("success", "Cập nhật danh mục thành công!");
            return "redirect:/admin/categories";
        } catch (Exception e) {
            log.error("Error updating category {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/categories/edit/" + id;
        }
    }

    /**
     * Xử lý xóa category
     */
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công!");
        } catch (Exception e) {
            log.error("Error deleting category {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}
