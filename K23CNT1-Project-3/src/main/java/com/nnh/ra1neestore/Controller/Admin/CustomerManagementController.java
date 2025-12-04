package com.nnh.ra1neestore.Controller.Admin;

import com.nnh.ra1neestore.Entity.User;
import com.nnh.ra1neestore.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller quản lý customers cho admin
 */
@Controller
@RequestMapping("/admin/customers")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CustomerManagementController {

    private final UserRepository userRepository;

    /**
     * Hiển thị danh sách tất cả users
     */
    /**
     * Hiển thị danh sách tất cả users
     */
    @GetMapping
    public String listCustomers(@RequestParam(required = false) String keyword, Model model) {
        java.util.List<User> users;
        if (keyword != null && !keyword.trim().isEmpty()) {
            users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
        } else {
            users = userRepository.findAll();
        }
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);

        return "admin/customers";
    }

    /**
     * Toggle user status (active/inactive)
     */
    @PostMapping("/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var user = userRepository.findById(id);

        if (user.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy user!");
            return "redirect:/admin/customers";
        }

        User u = user.get();

        // Toggle status
        if (u.getStatus() == User.UserStatus.active) {
            u.setStatus(User.UserStatus.inactive);
            redirectAttributes.addFlashAttribute("success",
                    "Đã vô hiệu hóa tài khoản " + u.getUsername());
        } else {
            u.setStatus(User.UserStatus.active);
            redirectAttributes.addFlashAttribute("success",
                    "Đã kích hoạt tài khoản " + u.getUsername());
        }

        userRepository.save(u);
        return "redirect:/admin/customers";
    }

    /**
     * Delete user
     */
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var user = userRepository.findById(id);

        if (user.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy user!");
            return "redirect:/admin/customers";
        }

        User u = user.get();

        // Prevent deleting yourself
        if (u.getRole() == User.UserRole.admin) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể xóa tài khoản admin! Hãy chuyển về customer trước.");
            return "redirect:/admin/customers";
        }

        userRepository.delete(u);
        redirectAttributes.addFlashAttribute("success",
                "Đã xóa tài khoản " + u.getUsername());

        return "redirect:/admin/customers";
    }

    /**
     * Show customer detail page
     */
    @GetMapping("/{id}/detail")
    public String showCustomerDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var user = userRepository.findById(id);

        if (user.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy user!");
            return "redirect:/admin/customers";
        }

        model.addAttribute("user", user.get());
        return "admin/customer-detail";
    }

    /**
     * Change customer password (only for customer role)
     */
    @PostMapping("/{id}/change-password")
    public String changeCustomerPassword(@PathVariable Long id,
                                         @RequestParam String newPassword,
                                         @RequestParam String confirmPassword,
                                         RedirectAttributes redirectAttributes) {
        var user = userRepository.findById(id);

        if (user.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy user!");
            return "redirect:/admin/customers";
        }

        User u = user.get();

        // Only allow password change for customer role
        if (u.getRole() == User.UserRole.admin) {
            redirectAttributes.addFlashAttribute("error", "Không thể thay đổi mật khẩu cho tài khoản admin!");
            return "redirect:/admin/customers/" + id + "/detail";
        }

        // Validate password
        if (newPassword == null || newPassword.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu không được để trống!");
            return "redirect:/admin/customers/" + id + "/detail";
        }

        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "redirect:/admin/customers/" + id + "/detail";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/admin/customers/" + id + "/detail";
        }

        // Update password (plain text, no encoding)
        u.setPassword(newPassword);
        userRepository.save(u);

        redirectAttributes.addFlashAttribute("success", "Đã thay đổi mật khẩu cho tài khoản " + u.getUsername());
        return "redirect:/admin/customers/" + id + "/detail";
    }
}
