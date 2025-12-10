package com.nnh.ra1neestore.Controller.Admin;

import com.nnh.ra1neestore.Entity.NnhOrder;
import com.nnh.ra1neestore.Entity.NnhOrderItem;
import com.nnh.ra1neestore.Service.NnhOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class NnhAdminOrderController {

    private final NnhOrderService orderService;

    // List all orders
    @GetMapping
    public String listOrders(Model model) {
        List<NnhOrder> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/orders/index";
    }

    // Order details
    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model) {
        NnhOrder order = orderService.getOrderById(orderId);
        List<NnhOrderItem> orderItems = orderService.getOrderItems(orderId);

        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("statuses", NnhOrder.OrderStatus.values());

        return "admin/orders/detail";
    }

    // Update order status
    @PostMapping("/{orderId}/status")
    public String updateStatus(@PathVariable Long orderId,
            @RequestParam NnhOrder.OrderStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái đơn hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/orders/" + orderId;
    }
}
