package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByIsActiveTrueOrderByDisplayOrderAsc();

    List<Banner> findAllByOrderByDisplayOrderAsc();
}
