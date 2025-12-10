package com.nnh.ra1neestore.Controller.User;

import com.nnh.ra1neestore.Config.CustomUserDetails;
import com.nnh.ra1neestore.Entity.NnhCart;
import com.nnh.ra1neestore.Entity.NnhOrder;
import com.nnh.ra1neestore.Entity.NnhOrderItem;
import com.nnh.ra1neestore.Service.NnhCartService;
import com.nnh.ra1neestore.Service.NnhOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NnhOrderController {

    private final NnhOrderService orderService;
    private final NnhCartService cartService;

    // Checkout page
    @GetMapping("/checkout")
    public String checkoutPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<NnhCart> cartItems = cartService.getCartItems(userDetails.getUserId());

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartService.getCartTotal(userDetails.getUserId()));

        return "user/checkout";
    }

    // Process checkout
    @PostMapping("/checkout")
    public String processCheckout(@RequestParam String customerName,
            @RequestParam String customerPhone,
            @RequestParam String customerAddress,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            NnhOrder order = orderService.createOrder(
                    userDetails.getUserId(),
                    customerName,
                    customerPhone,
                    customerAddress);

            redirectAttributes.addFlashAttribute("successMessage", "Đặt hàng thành công!");
            return "redirect:/order-success/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    // Order success page
    @GetMapping("/order-success/{orderId}")
    public String orderSuccess(@PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        NnhOrder order = orderService.getOrderById(orderId);
        List<NnhOrderItem> orderItems = orderService.getOrderItems(orderId);

        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);

        return "user/order-success";
    }

    // My orders
    @GetMapping("/nnhUser/orders")
    public String myOrders(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<NnhOrder> orders = orderService.getOrdersByUser(userDetails.getUserId());
        model.addAttribute("orders", orders);

        return "user/profile/my-orders";
    }

    // Order details
    @GetMapping("/nnhUser/orders/{orderId}")
    public String orderDetails(@PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        NnhOrder order = orderService.getOrderById(orderId);
        List<NnhOrderItem> orderItems = orderService.getOrderItems(orderId);

        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);

        return "user/profile/order-detail";
    }
}
