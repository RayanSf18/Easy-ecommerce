package com.rayan.easy_ecommerce.product;

import com.rayan.easy_ecommerce.category.Category;
import com.rayan.easy_ecommerce.category.CategoryService;
import com.rayan.easy_ecommerce.category.dto.CreateCategoryRequestPayload;
import com.rayan.easy_ecommerce.infra.exceptions.custom.*;
import com.rayan.easy_ecommerce.product.dto.CreateProductRequestPayload;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public Long createProduct(CreateProductRequestPayload payload) {
        validateProductName(payload.name());

        Product newProduct = new Product(null, payload.name(), payload.brand(), payload.model(), payload.description(), payload.price(), payload.imgUrl());
        productRepository.save(newProduct);

        for (String categoryName : payload.categories()) {
            Category category = getCategoryOrCreateIfNotFound(categoryName);
            newProduct.getCategories().add(category);
        }

        productRepository.save(newProduct);
        return newProduct.getId();
    }


    public Product getProduct(Long productId) {
        return this.productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }


    public Page<Product> getAllProducts(PageRequest pageable) {
        return this.productRepository.findAll(pageable);
    }


    @Transactional
    public Long updateProduct(Long productId, CreateProductRequestPayload payload) {
        Product existingProduct = getProduct(productId);
        updateProductData(existingProduct, payload);

        Set<Category> updatedCategories = new HashSet<>();
        for (String categoryName : payload.categories()) {
            Category category = getCategoryOrCreateIfNotFound(categoryName);
            updatedCategories.add(category);
        }

        updateProductCategories(existingProduct, updatedCategories);
        productRepository.save(existingProduct);
        return existingProduct.getId();
    }


    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
    }


    private void validateProductName(String name) {
        Optional<Product> existingProduct = productRepository.findByName(name);
        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistsException("There is already a product registered with this name.");
        }
    }


    private Category getCategoryOrCreateIfNotFound(String categoryName) {
        try {
            return categoryService.getByName(categoryName);
        } catch (CategoryNotFoundException e) {
            categoryService.createCategory(new CreateCategoryRequestPayload(categoryName));
            return categoryService.getByName(categoryName);
        }
    }


    private void updateProductData(Product product, CreateProductRequestPayload payload) {
        product.setName(payload.name());
        product.setBrand(payload.brand());
        product.setModel(payload.model());
        product.setDescription(payload.description());
        product.setPrice(payload.price());
        product.setImgUrl(payload.imgUrl());
    }


    private void updateProductCategories(Product product, Set<Category> updatedCategories) {
        product.getCategories().removeIf(category -> !updatedCategories.contains(category));
        for (Category category : updatedCategories) {
            if (!product.getCategories().contains(category)) {
                product.getCategories().add(category);
            }
        }
    }


}
