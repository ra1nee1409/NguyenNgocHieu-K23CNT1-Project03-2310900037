package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.NnhBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NnhBannerRepository extends JpaRepository<NnhBanner, Long> {
    
    /**
     * Tìm tất cả banners đang active, sắp xếp theo displayOrder
     */
    List<NnhBanner> findByIsActiveTrueOrderByDisplayOrderAsc();

    /**
     * Tìm tất cả banners, sắp xếp theo displayOrder
     */
    List<NnhBanner> findAllByOrderByDisplayOrderAsc();
}
