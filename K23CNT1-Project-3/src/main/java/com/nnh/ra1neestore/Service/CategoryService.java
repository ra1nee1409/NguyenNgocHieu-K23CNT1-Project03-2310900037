package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.Entity.Category;
import com.nnh.ra1neestore.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Get all categories
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Get category by ID
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Create new category
     */
    @Transactional
    public Category createCategory(Category category) {
        category.setCreatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    /**
     * Update existing category
     */
    @Transactional
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        
        return categoryRepository.save(existingCategory);
    }

    /**
     * Delete category
     */
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Check if category name exists
     */
    public boolean existsByName(String name) {
        return categoryRepository.findAll().stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
    }
}
