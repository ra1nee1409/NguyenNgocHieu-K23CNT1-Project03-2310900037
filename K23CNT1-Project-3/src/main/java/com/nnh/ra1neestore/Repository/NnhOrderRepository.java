package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.NnhOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NnhOrderRepository extends JpaRepository<NnhOrder, Long> {
    
    /**
     * Tìm tất cả đơn hàng của nnhUser, sắp xếp theo ngày tạo mới nhất
     */
    List<NnhOrder> findByNnhUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * Tìm đơn hàng theo trạng thái
     */
    List<NnhOrder> findByStatus(NnhOrder.OrderStatus status);
    
    /**
     * Tìm đơn hàng theo trạng thái, sắp xếp theo ngày tạo
     */
    List<NnhOrder> findByStatusOrderByCreatedAtDesc(NnhOrder.OrderStatus status);
    
    /**
     * Đếm số đơn hàng của nnhUser
     */
    long countByNnhUserId(Long userId);
}
