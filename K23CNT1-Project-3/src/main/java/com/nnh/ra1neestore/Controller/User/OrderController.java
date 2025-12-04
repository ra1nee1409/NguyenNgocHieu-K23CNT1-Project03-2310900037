package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Config.CustomUserDetails;
import com.nnh.ra1neestore.Entity.Cart;
import com.nnh.ra1neestore.Entity.Order;
import com.nnh.ra1neestore.Service.CartService;
import com.nnh.ra1neestore.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    /**
     * Trang checkout
     */
    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        // Lấy giỏ hàng
        List<Cart> cartItems = cartService.getCartItems(userDetails.getUser());
        
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        BigDecimal cartTotal = cartService.getCartTotal(userDetails.getUser());

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("user", userDetails.getUser());

        return "user/orders/checkout";
    }

    /**
     * Tạo đơn hàng
     */
    @PostMapping("/create")
    public String createOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestParam String customerName,
                             @RequestParam String customerPhone,
                             @RequestParam String customerAddress,
                             RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.createOrderFromCart(
                userDetails.getUser(),
                customerName,
                customerPhone,
                customerAddress
            );

            redirectAttributes.addFlashAttribute("success", 
                "Đặt hàng thành công! Mã đơn hàng: #" + order.getId());
            return "redirect:/orders/" + order.getId();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/checkout";
        }
    }

    /**
     * Lịch sử đơn hàng
     */
    @GetMapping("/history")
    public String orderHistory(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getOrdersByUser(userDetails.getUser());
        
        model.addAttribute("orders", orders);
        model.addAttribute("user", userDetails.getUser());

        return "user/orders/history";
    }

    /**
     * Chi tiết đơn hàng
     */
    @GetMapping("/{id}")
    public String orderDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @PathVariable Long id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.getOrderDetail(id, userDetails.getUser());
            
            model.addAttribute("order", order);
            model.addAttribute("user", userDetails.getUser());

            return "user/orders/detail";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/history";
        }
    }

    /**
     * Xác nhận đã nhận hàng
     */
    @PostMapping("/{id}/confirm-received")
    public String confirmReceived(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            orderService.confirmReceived(id, userDetails.getUser());
            redirectAttributes.addFlashAttribute("success", "Đã xác nhận nhận hàng thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/orders/" + id;
    }
}
