package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.NnhOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NnhOrderItemRepository extends JpaRepository<NnhOrderItem, Long> {

    List<NnhOrderItem> findByNnhOrderId(Long orderId);
}
