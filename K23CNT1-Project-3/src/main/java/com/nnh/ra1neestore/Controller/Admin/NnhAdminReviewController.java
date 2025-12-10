package com.nnh.ra1neestore.Controller.Admin;

import com.nnh.ra1neestore.Service.NnhReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class NnhAdminReviewController {

    private final NnhReviewService nnhReviewService;

    @GetMapping
    public String index(Model model) {
        var reviews = nnhReviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "admin/reviews/index";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            nnhReviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa đánh giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa đánh giá: " + e.getMessage());
        }
        return "redirect:/admin/reviews";
    }
}
