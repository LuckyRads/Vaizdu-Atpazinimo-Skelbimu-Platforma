package com.lucky.smartadplatform.application.rest.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    private String name;

    private List<CategoryDto> subcategories;

    private CategoryDto parentCategory;

}
