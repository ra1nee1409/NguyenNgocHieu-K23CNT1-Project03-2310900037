package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Config.CustomUserDetails;
import com.nnh.ra1neestore.Entity.User;
import com.nnh.ra1neestore.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controller for user profile settings
 */
@Controller
@RequestMapping("/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserRepository userRepository;

    /**
     * Show user profile settings page
     */
    @GetMapping("/settings")
    public String showSettings(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userDetails.getUser().getId()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user/profile/settings";
    }

    /**
     * Update personal information
     */
    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam String email,
                                @RequestParam(required = false) String fullName,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String address,
                                RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userDetails.getUser().getId()).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người dùng!");
            return "redirect:/user/profile/settings";
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Email không được để trống!");
            return "redirect:/user/profile/settings";
        }

        // Check if email already exists (for other users)
        Optional<User> existingUserOpt = userRepository.findByEmail(email);
        if (existingUserOpt.isPresent() && !existingUserOpt.get().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng bởi tài khoản khác!");
            return "redirect:/user/profile/settings";
        }

        // Update user information
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Đã cập nhật thông tin cá nhân thành công!");
        return "redirect:/user/profile/settings";
    }

    /**
     * Change password with old password verification
     */
    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @RequestParam String oldPassword,
                                  @RequestParam String newPassword,
                                  @RequestParam String confirmPassword,
                                  RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userDetails.getUser().getId()).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorPassword", "Không tìm thấy thông tin người dùng!");
            return "redirect:/user/profile/settings";
        }

        // Verify old password (plain text comparison)
        if (!user.getPassword().equals(oldPassword)) {
            redirectAttributes.addFlashAttribute("errorPassword", "Mật khẩu cũ không đúng!");
            return "redirect:/user/profile/settings";
        }

        // Validate new password
        if (newPassword == null || newPassword.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorPassword", "Mật khẩu mới không được để trống!");
            return "redirect:/user/profile/settings";
        }

        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("errorPassword", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            return "redirect:/user/profile/settings";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorPassword", "Mật khẩu xác nhận không khớp!");
            return "redirect:/user/profile/settings";
        }

        if (newPassword.equals(oldPassword)) {
            redirectAttributes.addFlashAttribute("errorPassword", "Mật khẩu mới phải khác mật khẩu cũ!");
            return "redirect:/user/profile/settings";
        }

        // Update password (plain text, no encoding)
        user.setPassword(newPassword);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successPassword", "Đã thay đổi mật khẩu thành công!");
        return "redirect:/user/profile/settings";
    }
}
