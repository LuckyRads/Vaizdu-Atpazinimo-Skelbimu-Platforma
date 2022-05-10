package com.lucky.smartadplatform.application.rest.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lucky.smartadplatform.domain.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    private String contactNumber;

    private List<ImageDto> images;

    private String category;

    private String owner;

    public ItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.description = item.getDescription();
        this.contactNumber = item.getContactNumber();
        if (item.getImages() != null) {
            this.images = new ArrayList<>();
            item.getImages().forEach(image -> this.images.add(new ImageDto(image)));
        }
        this.category = item.getCategory().getName();
        this.owner = item.getOwner().getEmail();
    }

}
