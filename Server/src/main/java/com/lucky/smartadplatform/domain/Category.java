package com.lucky.smartadplatform.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long id;

    private String name;

    private List<Category> subcategories;

    transient private Category parentCategory;

    transient private List<Item> items;

    public Category(String name, List<Category> categories, Category parentCategory, List<Item> items) {
        this.name = name;
        this.subcategories = categories;
        this.parentCategory = parentCategory;
        this.items = items;
    }

    public Category(String name, Category parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
    }

}
