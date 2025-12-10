package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Entity.NnhUser;
import com.nnh.ra1neestore.Repository.NnhUserRepository;
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
 * Controller xử lý authentication (login, register)
 */
@Controller
@RequiredArgsConstructor
public class NnhAuthController {

    private final NnhUserRepository nnhUserRepository;
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
        model.addAttribute("nnhUser", new NnhUser());
        return "user/register";
    }

    /**
     * Xử lý đăng ký
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute NnhUser nnhUser,
            RedirectAttributes redirectAttributes) {

        // Validate username uniqueness
        if (nnhUserRepository.existsByUsername(nnhUser.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username đã tồn tại!");
            return "redirect:/register";
        }

        // Validate email uniqueness
        if (nnhUserRepository.existsByEmail(nnhUser.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
            return "redirect:/register";
        }

        // Validate password
        if (nnhUser.getPassword() == null || nnhUser.getPassword().length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "redirect:/register";
        }

        // Set default values
        nnhUser.setRole(NnhUser.UserRole.CUSTOMER); // Mặc định là customer
        nnhUser.setStatus(NnhUser.UserStatus.ACTIVE); // Active ngay

        // Password không mã hóa (theo NoOpPasswordEncoder)
        // nnhUser.setPassword(passwordEncoder.encode(nnhUser.getPassword()));

        // Save user
        nnhUserRepository.save(nnhUser);

        redirectAttributes.addFlashAttribute("successMessage",
                "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");

        return "redirect:/login";
    }
}
