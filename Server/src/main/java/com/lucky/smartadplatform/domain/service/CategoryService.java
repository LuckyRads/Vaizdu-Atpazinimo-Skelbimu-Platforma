package com.lucky.smartadplatform.domain.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import com.lucky.smartadplatform.domain.Category;
import com.lucky.smartadplatform.domain.repository.CategoryRepository;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaCategory;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository<JpaCategory> categoryRepository;

    public Category createCategory(String name, Long parentCategoryId) {
        JpaCategory jpaParentCategory = null;
        Category parentCategory = null;
        try {
            jpaParentCategory = categoryRepository.findById(parentCategoryId).orElseThrow(NullPointerException::new);
            parentCategory = new Category();
            parentCategory.setId(jpaParentCategory.getId());
            parentCategory.setName(jpaParentCategory.getName());
            // parentCategory = modelMapper.map(jpaParentCategory, Category.class);
        } catch (Exception e) {
            // Ignore if category to be created is root.
        }
        Optional<JpaCategory> existingJpaCategory = categoryRepository.findByName(name);
        if (existingJpaCategory.isPresent()) {
            throw new IllegalArgumentException(String.format("Category with %s name already exists.", name));
        }

        Category category = new Category(name, parentCategory);

        JpaCategory jpaCategory = modelMapper.map(category, JpaCategory.class);
        categoryRepository.save(jpaCategory);
        category.setId(jpaCategory.getId());

        return category;
    }

    public List<Category> getCategories() {
        List<JpaCategory> jpaCategories = categoryRepository.findAll();

        Type type = new TypeToken<List<Category>>() {
        }.getType();
        List<Category> categories = modelMapper.map(jpaCategories, type);

        return categories;
    }

    public Category deleteCategory(Long id) {
        JpaCategory jpaCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Category does not exist."));

        categoryRepository.deleteById(jpaCategory.getId());

        return modelMapper.map(jpaCategory, Category.class);
    }

}
