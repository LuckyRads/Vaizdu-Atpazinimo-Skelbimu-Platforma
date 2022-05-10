package com.lucky.smartadplatform.application.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.lucky.smartadplatform.application.rest.model.CategoryDto;
import com.lucky.smartadplatform.application.rest.model.GenericResponse;
import com.lucky.smartadplatform.application.rest.model.StatusResponse;
import com.lucky.smartadplatform.application.rest.model.StatusResponse.StatusCode;
import com.lucky.smartadplatform.domain.Category;
import com.lucky.smartadplatform.domain.service.CategoryService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryService categoryService;

    @Secured({ "ROLE_ADMIN", "ROLE_MODERATOR" })
    @PostMapping
    public ResponseEntity<GenericResponse<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDto) {
        var response = new GenericResponse<CategoryDto>();

        Category category;
        try {
            category = categoryService.createCategory(categoryDto.getName(), categoryDto.getParentCategory().getId());
        } catch (Exception e) {
            response.setStatus(StatusCode.FAILED);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        categoryDto = modelMapper.map(category, CategoryDto.class);
        CategoryDto parentCategoryDto = new CategoryDto();
        parentCategoryDto.setId(category.getParentCategory().getId());
        parentCategoryDto.setName(category.getParentCategory().getName());
        categoryDto.setParentCategory(parentCategoryDto);
        response.setData(categoryDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<CategoryDto>>> getCategories() {
        var response = new GenericResponse<List<CategoryDto>>();

        List<Category> categories = categoryService.getCategories();
        List<CategoryDto> dtoCategories = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());

        response.setData(dtoCategories);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured({ "ROLE_ADMIN", "ROLE_MODERATOR" })
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusResponse> deleteCategory(@PathVariable("id") Long id) {
        var response = new StatusResponse();

        categoryService.deleteCategory(id);

        response.setMessage("Category deleted successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
