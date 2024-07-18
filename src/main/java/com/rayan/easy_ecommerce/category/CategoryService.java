package com.rayan.easy_ecommerce.category;

import com.rayan.easy_ecommerce.category.dto.CreateCategoryRequestPayload;
import com.rayan.easy_ecommerce.infra.exceptions.custom.CategoryAlreadyExistsException;
import com.rayan.easy_ecommerce.infra.exceptions.custom.CategoryNotFoundException;
import com.rayan.easy_ecommerce.infra.exceptions.custom.UserAlreadyExistsException;
import com.rayan.easy_ecommerce.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Long createCategory(CreateCategoryRequestPayload payload) {
        Optional<Category> existingCategory = this.categoryRepository.findByName(payload.name());
        if (existingCategory.isPresent()) throw new CategoryAlreadyExistsException("There is already a category registered with this name.");
        Category newCategory = new Category(null, payload.name());
        this.categoryRepository.save(newCategory);
        return newCategory.getId();
    }

    public Category getByName(String name) {
        Optional<Category> existingCategory = this.categoryRepository.findByName(name);
        if (existingCategory.isEmpty()) throw new CategoryNotFoundException("Category not found with name: " + name);
        return existingCategory.get();
    }

    public Page<Category> getAllCategories(PageRequest pageable) {
        return this.categoryRepository.findAll(pageable);
    }

    public Long updateCategory(Long categoryId, CreateCategoryRequestPayload payload) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new CategoryNotFoundException("Category not found with id: " + categoryId);
        }
        category.get().setName(payload.name());
        categoryRepository.save(category.get());
        return categoryId;
    }

    public void deleteCategory(String categoryName) {
        Category category = getByName(categoryName);
        categoryRepository.deleteById(category.getId());
    }
}
