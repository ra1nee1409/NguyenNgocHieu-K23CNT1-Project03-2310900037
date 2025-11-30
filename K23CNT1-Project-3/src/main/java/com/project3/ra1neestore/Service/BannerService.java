package com.project3.ra1neestore.Service;

import com.project3.ra1neestore.Entity.Banner;
import com.project3.ra1neestore.Repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;

    public List<Banner> getAllBanners() {
        return bannerRepository.findAllByOrderByDisplayOrderAsc();
    }

    public List<Banner> getActiveBanners() {
        return bannerRepository.findAllByIsActiveTrueOrderByDisplayOrderAsc();
    }

    public Optional<Banner> getBannerById(Long id) {
        return bannerRepository.findById(id);
    }

    @Transactional
    public Banner saveBanner(Banner banner) {
        return bannerRepository.save(banner);
    }

    @Transactional
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
}
