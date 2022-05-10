package com.lucky.smartadplatform.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.lucky.smartadplatform.domain.Category;
import com.lucky.smartadplatform.domain.repository.CategoryRepository;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaCategory;
import com.lucky.smartadplatform.util.TestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceUnitTests {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository<JpaCategory> categoryRepository;

    @Captor
    private ArgumentCaptor<JpaCategory> categoryArgumentCaptor;

    private JpaCategory categoryAll;
    private JpaCategory categoryClothing;
    private JpaCategory categoryClothingTops;

    @BeforeEach
    public void setUp() {
        categoryService = new CategoryService(new ModelMapper(), categoryRepository);
    }

    private void prepareCategories() {
        categoryAll = TestUtils.getTestCategory1();
        categoryClothing = TestUtils.getTestCategory2(categoryAll);
        categoryClothingTops = TestUtils.getTestCategory3(categoryClothing);
        categoryAll.setSubcategories(new ArrayList<>(Arrays.asList(categoryClothing)));
        categoryClothing.setSubcategories(new ArrayList<>(Arrays.asList(categoryClothingTops)));
    }

    @Test
    void testCreateCategoryExistentParentSuccess() {
        prepareCategories();

        JpaCategory parentCategory = categoryClothing;

        String name = "Clothing bottoms";
        Long parentCategoryId = 2L;

        Mockito.when(categoryRepository.findById(parentCategoryId)).thenReturn(Optional.of(parentCategory));
        Mockito.when(categoryRepository.findByName(name)).thenReturn(Optional.empty());

        Category category = categoryService.createCategory(name, parentCategoryId);

        assertEquals(name, category.getName());
        assertEquals(parentCategory.getName(), category.getParentCategory().getName());
        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryArgumentCaptor.capture());
    }

    @Test
    void testCreateCategoryRootSuccess() {
        String name = "All";

        Mockito.when(categoryRepository.findById(null)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.findByName(name)).thenReturn(Optional.empty());

        Category category = categoryService.createCategory(name, null);

        assertEquals(name, category.getName());
        assertEquals(null, category.getParentCategory());
        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryArgumentCaptor.capture());
    }

    @Test
    void testDeleteCategoryChildSuccess() {
        prepareCategories();

        JpaCategory rootCategory = categoryAll;
        JpaCategory category = categoryClothing;

        Long id = 2L;

        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Category deletedCategory = categoryService.deleteCategory(id);

        assertEquals(id, deletedCategory.getId());
        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(any());
    }

    @Test
    void testDeleteCategoryParentSuccess() {
        prepareCategories();

        JpaCategory rootCategory = categoryAll;
        JpaCategory parentCategory = categoryClothing;
        JpaCategory childCategory = categoryClothingTops;

        Long id = 2L;

        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(parentCategory));

        Category deletedCategory = categoryService.deleteCategory(id);

        assertEquals(id, deletedCategory.getId());
        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(any());
    }

    @Test
    void testGetCategoriesAllSuccess() {
        prepareCategories();

        JpaCategory category1 = categoryAll;
        JpaCategory category2 = categoryClothing;
        JpaCategory category3 = categoryClothingTops;

        Mockito.when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2, category3));

        List<Category> categories = categoryService.getCategories();

        assertEquals(3, categories.size());
        assertEquals("All", categories.get(0).getName());
        assertEquals("Clothing", categories.get(1).getName());
        assertEquals("Clothing tops", categories.get(2).getName());
    }

}
