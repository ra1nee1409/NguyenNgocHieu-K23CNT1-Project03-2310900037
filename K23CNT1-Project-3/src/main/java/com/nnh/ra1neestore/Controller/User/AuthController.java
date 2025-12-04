package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Entity.User;
import com.nnh.ra1neestore.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller xử lý authentication
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Trang login
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Username hoặc password không đúng!");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "Đăng xuất thành công!");
        }

        return "user/login";
    }

    /**
     * Hiển thị form đăng ký
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    /**
     * Xử lý đăng ký
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
            RedirectAttributes redirectAttributes) {

        // Validate username uniqueness
        if (userRepository.existsByUsername(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username đã tồn tại!");
            return "redirect:/register";
        }

        // Validate email uniqueness
        if (userRepository.existsByEmail(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
            return "redirect:/register";
        }

        // Validate password
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "redirect:/register";
        }

        // Set default values
        user.setRole(User.UserRole.customer); // Mặc định là customer
        user.setStatus(User.UserStatus.active); // Active ngay

        // Encode password - TẮT để dùng plain text theo yêu cầu
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success",
                "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");

        return "redirect:/login";
    }

    /**
     * Trang access denied
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "user/access-denied";
    }

}
