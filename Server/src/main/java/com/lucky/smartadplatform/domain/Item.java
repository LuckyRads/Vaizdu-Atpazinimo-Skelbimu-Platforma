package com.lucky.smartadplatform.domain;

import java.math.BigDecimal;
import java.util.List;

import com.lucky.smartadplatform.application.rest.model.ItemDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Item {

    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    private String contactNumber;

    private Image mainImage;

    private List<Image> images;

    private Category category;

    private User owner;

    public Item(String name, BigDecimal price, String description, String contactNumber, List<Image> images,
            Category category,
            User owner) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.contactNumber = contactNumber;
        this.images = images;
        this.category = category;
        this.owner = owner;
    }

    public void updateItem(Category category, List<Image> images) {
        this.category = category;
        this.images = images;
    }

    public void updateNonRelationalProperties(ItemDto itemDto) {
        this.name = itemDto.getName();
        this.price = itemDto.getPrice();
        this.description = itemDto.getDescription();
        this.contactNumber = itemDto.getContactNumber();
    }

    public void validateState() {
        if (this.name == null || this.name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (this.description == null || this.description.isBlank()) {
            throw new IllegalArgumentException("Invalid description");
        }
        if (this.category == null) {
            throw new IllegalStateException("Invalid category");
        }
        if (this.owner == null) {
            throw new IllegalStateException("Invalid owner");
        }
    }

}
