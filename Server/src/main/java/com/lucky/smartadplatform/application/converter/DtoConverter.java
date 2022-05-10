package com.lucky.smartadplatform.application.converter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lucky.smartadplatform.application.rest.model.CategoryDto;
import com.lucky.smartadplatform.application.rest.model.ImageDto;
import com.lucky.smartadplatform.application.rest.model.ItemDto;
import com.lucky.smartadplatform.domain.Category;
import com.lucky.smartadplatform.domain.Item;

import org.modelmapper.AbstractConverter;

public class DtoConverter {

    private DtoConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static AbstractConverter<Category, CategoryDto> getCategoryToDtoConverter() {
        return new AbstractConverter<Category, CategoryDto>() {

            @Override
            protected CategoryDto convert(Category source) {
                CategoryDto destination = new CategoryDto();
                destination.setId(source.getId());
                destination.setName(source.getName());
                if (source.getParentCategory() != null) {
                    CategoryDto parentCategoryDto = new CategoryDto();
                    parentCategoryDto.setId(source.getParentCategory().getId());
                    parentCategoryDto.setName(source.getParentCategory().getName());
                    destination.setParentCategory(parentCategoryDto);
                }

                String subcategoriesJson = new Gson().toJson(source.getSubcategories());
                Type type = new TypeToken<List<CategoryDto>>() {
                }.getType();
                List<CategoryDto> subcategories = new Gson().fromJson(subcategoriesJson, type);
                destination.setSubcategories(subcategories);
                return destination;
            }

        };
    }

    public static AbstractConverter<Item, ItemDto> getItemToDtoConverter() {
        return new AbstractConverter<Item, ItemDto>() {

            @Override
            protected ItemDto convert(Item source) {
                ItemDto destination = new ItemDto();
                destination.setId(source.getId());
                destination.setCategory(source.getCategory().getName());
                destination.setDescription(source.getDescription());
                destination.setContactNumber(source.getContactNumber());
                destination.setName(source.getName());
                destination.setPrice(source.getPrice());
                destination.setOwner(source.getOwner().getUsername());

                List<ImageDto> dtoImages = new ArrayList<>();
                source.getImages().forEach(image -> {
                    ImageDto imageDto = new ImageDto(image);
                    dtoImages.add(imageDto);
                });
                destination.setImages(dtoImages);

                return destination;
            }

        };
    }

}
