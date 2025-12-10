package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.Entity.NnhBanner;
import com.nnh.ra1neestore.Repository.NnhBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service xử lý business logic cho Banner
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NnhBannerService {

    private final NnhBannerRepository nnhBannerRepository;

    /**
     * Lấy tất cả banners đang active, sắp xếp theo displayOrder
     */
    public List<NnhBanner> getActiveBanners() {
        return nnhBannerRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    /**
     * Lấy tất cả banners (bao gồm inactive)
     */
    public List<NnhBanner> getAllBanners() {
        return nnhBannerRepository.findAllByOrderByDisplayOrderAsc();
    }

    /**
     * Lấy banner theo ID
     */
    public Optional<NnhBanner> getBannerById(Long id) {
        return nnhBannerRepository.findById(id);
    }

    /**
     * Tạo banner mới
     */
    public NnhBanner createBanner(NnhBanner nnhBanner) {
        return nnhBannerRepository.save(nnhBanner);
    }

    /**
     * Lưu banner (alias cho create/update)
     */
    public NnhBanner saveBanner(NnhBanner nnhBanner) {
        return nnhBannerRepository.save(nnhBanner);
    }

    /**
     * Cập nhật banner
     */
    public NnhBanner updateBanner(Long id, NnhBanner nnhBanner) {
        return nnhBannerRepository.findById(id)
                .map(existing -> {
                    existing.setName(nnhBanner.getName());
                    existing.setImageUrl(nnhBanner.getImageUrl());
                    existing.setDisplayOrder(nnhBanner.getDisplayOrder());
                    existing.setIsActive(nnhBanner.getIsActive());
                    return nnhBannerRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));
    }

    /**
     * Xóa banner
     */
    public void deleteBanner(Long id) {
        nnhBannerRepository.deleteById(id);
    }
}
