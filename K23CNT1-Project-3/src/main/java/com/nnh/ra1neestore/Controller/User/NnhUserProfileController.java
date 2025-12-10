package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Config.CustomUserDetails;
import com.nnh.ra1neestore.DTO.PasswordChangeDTO;
import com.nnh.ra1neestore.DTO.ProfileUpdateDTO;
import com.nnh.ra1neestore.Entity.NnhUser;
import com.nnh.ra1neestore.Service.NnhUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/nnhUser/profile")
@RequiredArgsConstructor
public class NnhUserProfileController {

    private final NnhUserService nnhUserService;

    @GetMapping("/settings")
    public String settings(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        NnhUser user = userDetails.getNnhUser();
        model.addAttribute("user", user);
        return "user/profile/settings";
    }

    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute ProfileUpdateDTO dto,
            RedirectAttributes redirectAttributes) {
        try {
            NnhUser user = userDetails.getNnhUser();
            nnhUserService.updateProfile(user, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/nnhUser/profile/settings";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute PasswordChangeDTO dto,
            RedirectAttributes redirectAttributes) {
        try {
            NnhUser user = userDetails.getNnhUser();

            if (!nnhUserService.verifyCurrentPassword(user, dto.getCurrentPassword())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu hiện tại không đúng!");
                return "redirect:/nnhUser/profile/settings";
            }

            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới không khớp!");
                return "redirect:/nnhUser/profile/settings";
            }

            nnhUserService.changePassword(user, dto.getNewPassword());
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/nnhUser/profile/settings";
    }
}
