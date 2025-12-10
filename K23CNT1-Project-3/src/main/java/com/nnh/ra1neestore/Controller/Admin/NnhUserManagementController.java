package com.nnh.ra1neestore.Controller.Admin;

import com.nnh.ra1neestore.Entity.NnhUser;
import com.nnh.ra1neestore.Repository.NnhUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/nnhUsers")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class NnhUserManagementController {

    private final NnhUserRepository nnhUserRepository;

    @GetMapping
    public String index(@RequestParam(required = false) String keyword,
            Model model) {
        var users = keyword != null && !keyword.trim().isEmpty()
                ? nnhUserRepository.findByUsernameContainingOrFullNameContaining(keyword, keyword)
                : nnhUserRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        return "admin/users/index";
    }

    @PostMapping("/toggleStatus/{id}")
    public String toggleStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var user = nnhUserRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            // Toggle between ACTIVE and INACTIVE
            if (user.getStatus() == NnhUser.UserStatus.ACTIVE) {
                user.setStatus(NnhUser.UserStatus.INACTIVE);
            } else {
                user.setStatus(NnhUser.UserStatus.ACTIVE);
            }
            nnhUserRepository.save(user);

            redirectAttributes.addFlashAttribute("success",
                    user.getStatus() == NnhUser.UserStatus.ACTIVE ? "Kích hoạt user thành công!"
                            : "Vô hiệu hóa user thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nnhUsers";
    }
}
