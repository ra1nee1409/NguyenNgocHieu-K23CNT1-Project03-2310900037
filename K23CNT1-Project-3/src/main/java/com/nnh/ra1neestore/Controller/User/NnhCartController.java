package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Config.CustomUserDetails;
import com.nnh.ra1neestore.Entity.NnhCart;
import com.nnh.ra1neestore.Service.NnhCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class NnhCartController {

    private final NnhCartService cartService;

    @GetMapping
    public String viewCart(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<NnhCart> cartItems = cartService.getCartItems(userDetails.getUserId());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartService.getCartTotal(userDetails.getUserId()));

        return "user/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            cartService.addToCart(userDetails.getUserId(), productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm vào giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }

        return "redirect:/product/" + productId;
    }

    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable Long id,
            @RequestParam Integer quantity,
            RedirectAttributes redirectAttributes) {
        try {
            cartService.updateQuantity(id, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cartService.removeFromCart(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa khỏi giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            cartService.clearCart(userDetails.getUserId());
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa toàn bộ giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }

        return "redirect:/cart";
    }
}
