package com.nnh.ra1neestore.Controller.Admin;

import com.nnh.ra1neestore.Entity.Order;
import com.nnh.ra1neestore.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    /**
     * Danh sách đơn hàng với filter
     */
    @GetMapping
    public String listOrders(@RequestParam(required = false) String status,
                            @RequestParam(required = false) String search,
                            Model model) {
        List<Order> orders;

        // Filter by status
        if (status != null && !status.isEmpty() && !status.equals("ALL")) {
            try {
                Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status);
                orders = orderService.getOrdersByStatus(orderStatus);
            } catch (IllegalArgumentException e) {
                orders = orderService.getAllOrders();
            }
        } else {
            orders = orderService.getAllOrders();
        }

        // Search by order ID or customer name
        if (search != null && !search.isEmpty()) {
            orders = orders.stream()
                    .filter(order -> 
                        String.valueOf(order.getId()).contains(search) ||
                        (order.getCustomerName() != null && order.getCustomerName().toLowerCase().contains(search.toLowerCase()))
                    )
                    .toList();
        }

        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", status != null ? status : "ALL");
        model.addAttribute("searchQuery", search != null ? search : "");

        return "admin/orders/index";
    }

    /**
     * Chi tiết đơn hàng
     */
    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.getAllOrders().stream()
                    .filter(o -> o.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));

            model.addAttribute("order", order);
            return "admin/orders/detail";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/orders";
        }
    }

    /**
     * Xác nhận đơn hàng (PENDING → CONFIRMED)
     */
    @PostMapping("/{id}/confirm")
    public String confirmOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, Order.OrderStatus.CONFIRMED);
            redirectAttributes.addFlashAttribute("success", "Đã xác nhận đơn hàng!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    /**
     * Chuyển sang giao hàng (CONFIRMED → SHIPPING)
     */
    @PostMapping("/{id}/ship")
    public String shipOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, Order.OrderStatus.SHIPPING);
            redirectAttributes.addFlashAttribute("success", "Đã chuyển sang trạng thái giao hàng!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    /**
     * Đã tới điểm giao (SHIPPING → DELIVERED)
     */
    @PostMapping("/{id}/deliver")
    public String deliverOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, Order.OrderStatus.DELIVERED);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật: Đơn hàng đã tới điểm giao!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    /**
     * Hủy đơn hàng (hoàn tồn kho)
     */
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng và hoàn lại tồn kho!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }
}
