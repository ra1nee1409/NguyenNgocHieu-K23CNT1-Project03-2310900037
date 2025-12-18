package com.nnh.ra1neestore.Controller;

import com.nnh.ra1neestore.Service.EmailService;
import com.nnh.ra1neestore.Service.NnhUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NnhPasswordResetController {

    private final NnhUserService nnhUserService;
    private final EmailService emailService;

    @Value("${password.reset.link.base-url}")
    private String baseUrl;

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            String token = nnhUserService.createPasswordResetToken(email);

            if (token == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Email không tồn tại trong hệ thống!");
                return "redirect:/forgot-password";
            }

            String resetLink = baseUrl + "/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(email, resetLink);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Email reset mật khẩu đã được gửi! Vui lòng kiểm tra hộp thư.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }

        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {
        log.debug("Accessing reset password page with token");

        boolean isValid = nnhUserService.validatePasswordResetToken(token);

        if (!isValid) {
            log.warn("Invalid or expired password reset token");
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Link reset mật khẩu không hợp lệ hoặc đã hết hạn!");
            return "redirect:/login";
        }

        model.addAttribute("token", token);
        log.debug("Displaying reset password page");
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu không khớp!");
            return "redirect:/reset-password?token=" + token;
        }

        if (nnhUserService.resetPassword(token, newPassword)) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đổi mật khẩu thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Link reset mật khẩu không hợp lệ hoặc đã hết hạn!");
            return "redirect:/login";
        }
    }
}
