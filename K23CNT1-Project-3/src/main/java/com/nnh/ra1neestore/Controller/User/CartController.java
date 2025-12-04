package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Config.CustomUserDetails;
import com.nnh.ra1neestore.Entity.Cart;
import com.nnh.ra1neestore.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    @PostMapping("/add")
    public String addToCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @RequestParam Long productId,
                           @RequestParam(defaultValue = "1") Integer quantity,
                           @RequestParam(required = false) String returnUrl,
                           RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
            return "redirect:/login";
        }

        try {
            cartService.addToCart(userDetails.getUser(), productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        // Redirect về trang trước đó hoặc trang chủ
        return "redirect:" + (returnUrl != null && !returnUrl.isEmpty() ? returnUrl : "/home");
    }

    /**
     * Xem giỏ hàng
     */
    @GetMapping
    public String viewCart(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<Cart> cartItems = cartService.getCartItems(userDetails.getUser());
        BigDecimal cartTotal = cartService.getCartTotal(userDetails.getUser());

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("user", userDetails.getUser());

        return "user/cart/index";
    }

    /**
     * Cập nhật số lượng sản phẩm
     */
    @PostMapping("/update/{id}")
    public String updateQuantity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @PathVariable Long id,
                                 @RequestParam Integer quantity,
                                 RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            cartService.updateQuantity(id, quantity, userDetails.getUser());
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật số lượng!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cart";
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    @PostMapping("/remove/{id}")
    public String removeFromCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            cartService.removeFromCart(id, userDetails.getUser());
            redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm khỏi giỏ hàng!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cart";
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        cartService.clearCart(userDetails.getUser());
        redirectAttributes.addFlashAttribute("success", "Đã xóa toàn bộ giỏ hàng!");

        return "redirect:/cart";
    }
}
