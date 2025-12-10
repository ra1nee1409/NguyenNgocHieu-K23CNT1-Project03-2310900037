package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.Entity.*;
import com.nnh.ra1neestore.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NnhOrderService {

    private final NnhOrderRepository orderRepository;
    private final NnhOrderItemRepository orderItemRepository;
    private final NnhCartRepository cartRepository;
    private final NnhProductRepository productRepository;

    @Transactional
    public NnhOrder createOrder(Long userId, String customerName, String customerPhone, String customerAddress) {
        // Get cart items
        List<NnhCart> cartItems = cartRepository.findByNnhUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống!");
        }

        // Validate stock and calculate total
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (NnhCart cartItem : cartItems) {
            NnhProduct product = cartItem.getNnhProduct();

            // Check stock
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Sản phẩm '" + product.getName() + "' không đủ số lượng trong kho!");
            }

            totalAmount = totalAmount.add(
                    product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        // Create order
        NnhUser user = new NnhUser();
        user.setId(userId);

        NnhOrder order = NnhOrder.builder()
                .nnhUser(user)
                .totalAmount(totalAmount)
                .status(NnhOrder.OrderStatus.PENDING)
                .customerName(customerName)
                .customerPhone(customerPhone)
                .customerAddress(customerAddress)
                .build();

        order = orderRepository.save(order);

        // Create order items and deduct product quantity
        for (NnhCart cartItem : cartItems) {
            NnhProduct product = cartItem.getNnhProduct();

            // Create order item
            NnhOrderItem orderItem = NnhOrderItem.builder()
                    .nnhOrder(order)
                    .nnhProduct(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            orderItemRepository.save(orderItem);

            // **DEDUCT PRODUCT QUANTITY**
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // Clear cart
        cartRepository.deleteByNnhUserId(userId);

        return order;
    }

    public List<NnhOrder> getOrdersByUser(Long userId) {
        return orderRepository.findByNnhUserIdOrderByCreatedAtDesc(userId);
    }

    public List<NnhOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public NnhOrder getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<NnhOrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByNnhOrderId(orderId);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, NnhOrder.OrderStatus status) {
        NnhOrder order = getOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }

    public long countOrdersByUser(Long userId) {
        return orderRepository.countByNnhUserId(userId);
    }
}
