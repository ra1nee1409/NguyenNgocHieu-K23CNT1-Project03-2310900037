package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.Entity.NnhCategory;
import com.nnh.ra1neestore.Repository.NnhCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service xử lý business logic cho Category
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NnhCategoryService {

    private final NnhCategoryRepository nnhCategoryRepository;

    /**
     * Lấy tất cả categories
     */
    public List<NnhCategory> getAllCategories() {
        return nnhCategoryRepository.findAll();
    }

    /**
     * Lấy category theo ID
     */
    public Optional<NnhCategory> getCategoryById(Long id) {
        return nnhCategoryRepository.findById(id);
    }

    /**
     * Tạo category mới
     */
    public NnhCategory createCategory(NnhCategory nnhCategory) {
        return nnhCategoryRepository.save(nnhCategory);
    }

    /**
     * Cập nhật category
     */
    public NnhCategory updateCategory(Long id, NnhCategory nnhCategory) {
        return nnhCategoryRepository.findById(id)
            .map(existing -> {
                existing.setName(nnhCategory.getName());
                existing.setDescription(nnhCategory.getDescription());
                return nnhCategoryRepository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    /**
     * Xóa category
     */
    public void deleteCategory(Long id) {
        nnhCategoryRepository.deleteById(id);
    }
}
