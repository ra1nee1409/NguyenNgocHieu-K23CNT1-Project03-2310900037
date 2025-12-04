package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.Entity.*;
import com.nnh.ra1neestore.Repository.CartRepository;
import com.nnh.ra1neestore.Repository.OrderRepository;
import com.nnh.ra1neestore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    /**
     * Tạo đơn hàng từ giỏ hàng
     */
    @Transactional
    public Order createOrderFromCart(User user, String customerName, String customerPhone, String customerAddress) {
        // Lấy tất cả items trong giỏ hàng
        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống! Vui lòng thêm sản phẩm trước khi đặt hàng.");
        }

        // Tính tổng tiền và tạo order items
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            
            // Kiểm tra tồn kho
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Sản phẩm '" + product.getName() + "' không đủ số lượng trong kho!");
            }

            // Lấy giá (ưu tiên sale price)
            BigDecimal unitPrice = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
            
            // Tạo order item
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(unitPrice)
                    .build();
            
            orderItems.add(orderItem);
            
            // Cộng vào tổng tiền
            totalAmount = totalAmount.add(unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            
            // Trừ tồn kho
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // Tạo đơn hàng
        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .status(Order.OrderStatus.PENDING)
                .customerName(customerName)
                .customerPhone(customerPhone)
                .customerAddress(customerAddress)
                .createdAt(LocalDateTime.now())
                .orderItems(new ArrayList<>())
                .build();

        // Lưu order trước
        order = orderRepository.save(order);

        // Set order cho các order items và thêm vào order
        for (OrderItem item : orderItems) {
            item.setOrder(order);
            order.getOrderItems().add(item);
        }

        // Lưu lại order với order items
        order = orderRepository.save(order);

        // Xóa giỏ hàng
        cartRepository.deleteByUserId(user.getId());

        return order;
    }

    /**
     * Lấy danh sách đơn hàng của user
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    /**
     * Lấy chi tiết đơn hàng
     */
    @Transactional(readOnly = true)
    public Order getOrderDetail(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
        
        // Kiểm tra quyền sở hữu
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền xem đơn hàng này!");
        }
        
        return order;
    }

    /**
     * User xác nhận đã nhận hàng
     */
    @Transactional
    public void confirmReceived(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
        
        // Kiểm tra quyền sở hữu
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền thực hiện thao tác này!");
        }
        
        // Chỉ cho phép xác nhận khi đơn hàng ở trạng thái DELIVERED
        if (order.getStatus() != Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Chỉ có thể xác nhận nhận hàng khi đơn hàng đã được giao!");
        }
        
        order.setStatus(Order.OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    /**
     * Lấy tất cả đơn hàng (cho admin)
     */
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Lấy đơn hàng theo trạng thái (cho admin)
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    /**
     * Cập nhật trạng thái đơn hàng (cho admin)
     */
    @Transactional
    public void updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
        
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    /**
     * Hủy đơn hàng và hoàn lại tồn kho
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
        
        // Chỉ cho phép hủy đơn hàng ở trạng thái PENDING hoặc CONFIRMED
        if (order.getStatus() != Order.OrderStatus.PENDING && order.getStatus() != Order.OrderStatus.CONFIRMED) {
            throw new RuntimeException("Không thể hủy đơn hàng ở trạng thái này!");
        }
        
        // Hoàn lại tồn kho
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
