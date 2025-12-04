package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Tìm tất cả đơn hàng của user, sắp xếp theo ngày tạo mới nhất
     */
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * Tìm đơn hàng theo trạng thái
     */
    List<Order> findByStatus(Order.OrderStatus status);
    
    /**
     * Tìm đơn hàng theo trạng thái, sắp xếp theo ngày tạo
     */
    List<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status);
    
    /**
     * Đếm số đơn hàng của user
     */
    long countByUserId(Long userId);
}
