package com.lucky.smartadplatform.domain.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.lucky.smartadplatform.application.rest.model.ItemDto;
import com.lucky.smartadplatform.domain.Category;
import com.lucky.smartadplatform.domain.Image;
import com.lucky.smartadplatform.domain.Item;
import com.lucky.smartadplatform.domain.User;
import com.lucky.smartadplatform.domain.repository.CategoryRepository;
import com.lucky.smartadplatform.domain.repository.ImageRepository;
import com.lucky.smartadplatform.domain.repository.ItemRepository;
import com.lucky.smartadplatform.domain.repository.UserRepository;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaCategory;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaImage;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaItem;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ItemService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ItemRepository<JpaItem> itemRepository;

    @Autowired
    private CategoryRepository<JpaCategory> categoryRepository;

    @Autowired
    private UserRepository<JpaUser> userRepository;

    @Autowired
    private ImageRepository<JpaImage> imageRepository;

    @Transactional
    public Item createItem(String name, BigDecimal price, String description, String contactNumber, String categoryName,
            String ownerName,
            List<Long> imageIds) throws IOException {
        Optional<JpaCategory> optionalJpaCategory = categoryRepository.findByName(categoryName);
        if (!optionalJpaCategory.isPresent()) {
            throw new NullPointerException(String.format("Category %s not found.", categoryName));
        }

        JpaCategory jpaCategory = optionalJpaCategory.get();
        Category category = modelMapper.map(jpaCategory, Category.class);

        JpaUser jpaUser = userRepository.findByUsername(ownerName)
                .orElseThrow(() -> new NullPointerException(String.format("User %s not found.", ownerName)));
        User user = modelMapper.map(jpaUser, User.class);

        List<JpaImage> jpaItemImages = imageService.getUploadedItemImages(ownerName, imageIds);
        Type imageListType = new TypeToken<List<Image>>() {
        }.getType();
        List<Image> itemImages = modelMapper.map(jpaItemImages, imageListType);

        Item item = new Item(name, price, description, contactNumber, itemImages, category, user);
        JpaItem jpaItem = modelMapper.map(item, JpaItem.class);
        jpaItem.setOwner(jpaUser);
        itemRepository.save(jpaItem);
        item.setId(jpaItem.getId());

        try {
            for (Image image : item.getImages()) {
                image.setItem(item);
                imageService.moveImageFromTempToUserItem(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to move image to item dir.");
        }

        jpaItemImages.forEach(image -> {
            image.setItem(jpaItem);
            imageRepository.save(image);

        });

        item.setId(jpaItem.getId());
        return item;
    }

    public Item updateItem(Long id, List<Long> imageIds, ItemDto itemDto, String currentUser) throws IOException {
        JpaItem jpaItem = itemRepository.findById(id).orElseThrow(() -> new NullPointerException("Item not found."));
        Item item = modelMapper.map(jpaItem, Item.class);
        List<Long> existingImageIds = item.getImages().stream().map(Image::getId).collect(Collectors.toList());

        if (!item.getOwner().getUsername().equals(currentUser))
            throw new IllegalAccessError("Current user cannot update the specified item.");

        JpaCategory jpaCategory = categoryRepository.findByName(itemDto.getCategory())
                .orElseThrow(() -> new NullPointerException("Specified category does not exist."));
        Category category = modelMapper.map(jpaCategory, Category.class);

        List<JpaImage> jpaItemImages = imageService.getUploadedItemImages(currentUser, imageIds);
        JpaUser jpaUser = jpaItem.getOwner();

        Type imageListType = new TypeToken<List<Image>>() {
        }.getType();
        List<Image> itemImages = modelMapper.map(jpaItemImages, imageListType);

        item.updateItem(category, itemImages);
        item.updateNonRelationalProperties(itemDto);

        try {
            for (Image image : item.getImages()) {
                image.setItem(item);
                if (!existingImageIds.contains(image.getId()))
                    imageService.moveImageFromTempToUserItem(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to move image to item dir.");
        }

        jpaItem = modelMapper.map(item, JpaItem.class);
        jpaItem.setOwner(jpaUser);
        for (JpaImage jpaItemImage : jpaItemImages) {
            jpaItemImage.setItem(jpaItem);
            imageRepository.save(jpaItemImage);
        }
        try {
            itemRepository.save(jpaItem);
        } catch (InvalidDataAccessApiUsageException e) {
            updateItem(id, imageIds, itemDto, currentUser);
        }

        return item;
    }

    @Transactional
    public Item deleteItem(Long id, String currentUsername) {
        JpaItem jpaItem = itemRepository.findById(id).orElseThrow(() -> new NullPointerException("Item not found."));
        Item item = modelMapper.map(jpaItem, Item.class);

        if (!item.getOwner().getUsername().equals(currentUsername))
            throw new IllegalAccessError("Unauthorized!");

        for (Image image : item.getImages()) {
            try {
                imageService.deleteImage(image.getId(), currentUsername);
            } catch (Exception e) {
                log.error("Image deletion failed.");
                e.printStackTrace();
            }
        }

        itemRepository.deleteById(jpaItem.getId());

        return item;
    }

    public List<Item> getItems(Long categoryId) {
        JpaCategory jpaCategory;
        try {
            jpaCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NullPointerException("Category not found."));
        } catch (Exception e) {
            jpaCategory = categoryRepository.findByName("All")
                    .orElseThrow(() -> new NullPointerException("Category not found."));
        }

        List<JpaItem> jpaItems = jpaCategory.getItems();
        List<JpaCategory> subcategories = jpaCategory.getSubcategories();
        while (!subcategories.isEmpty()) {
            List<JpaCategory> nextSubcategories = new ArrayList<>();
            for (JpaCategory subcategory : subcategories) {
                jpaItems.addAll(subcategory.getItems());
                if (subcategory.getSubcategories() != null)
                    nextSubcategories.addAll(subcategory.getSubcategories());
            }
            subcategories = nextSubcategories;
        }

        Type itemsListType = new TypeToken<List<Item>>() {
        }.getType();
        List<Item> items = modelMapper.map(jpaItems, itemsListType);

        for (Item item : items) {
            List<Image> newItemImages = new ArrayList<>();
            for (Image image : item.getImages()) {
                try {
                    image.setData(imageService.loadImageDataFromDisk(image));
                    newItemImages.add(image);
                } catch (Exception e) {
                    log.error("Failed to load image.");
                    e.printStackTrace();
                }
            }
            item.setImages(newItemImages);
        }

        return items;
    }

    public List<Item> getFilteredItems(Long categoryId, BigDecimal fromPrice, BigDecimal toPrice) {
        JpaCategory jpaCategory;
        try {
            jpaCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NullPointerException("Category not found."));
        } catch (Exception e) {
            jpaCategory = categoryRepository.findByName("All")
                    .orElseThrow(() -> new NullPointerException("Category not found."));
        }

        List<JpaItem> jpaItems = jpaCategory.getItems();
        List<JpaCategory> subcategories = jpaCategory.getSubcategories();
        while (!subcategories.isEmpty()) {
            List<JpaCategory> nextSubcategories = new ArrayList<>();
            for (JpaCategory subcategory : subcategories) {
                jpaItems.addAll(subcategory.getItems());
                if (subcategory.getSubcategories() != null)
                    nextSubcategories.addAll(subcategory.getSubcategories());
            }
            subcategories = nextSubcategories;
        }
        Type itemsListType = new TypeToken<List<Item>>() {
        }.getType();
        List<Item> items = modelMapper.map(jpaItems, itemsListType);

        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (fromPrice != null && item.getPrice().compareTo(fromPrice) < 0)
                continue;
            if (toPrice != null && item.getPrice().compareTo(toPrice) > 0)
                continue;

            List<Image> newItemImages = new ArrayList<>();
            for (Image image : item.getImages()) {
                try {
                    image.setData(imageService.loadImageDataFromDisk(image));
                    newItemImages.add(image);
                } catch (Exception e) {
                    log.error("Failed to load image.");
                    e.printStackTrace();
                }
            }
            item.setImages(newItemImages);
            filteredItems.add(item);
        }

        return filteredItems;
    }

    public List<Item> getOwnedItems(Long categoryId, String currentUser, BigDecimal fromPrice, BigDecimal toPrice) {
        JpaCategory jpaCategory;
        try {
            jpaCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NullPointerException("Category not found."));
        } catch (Exception e) {
            jpaCategory = categoryRepository.findByName("All")
                    .orElseThrow(() -> new NullPointerException("Category not found."));
        }

        List<JpaItem> jpaItems = jpaCategory.getItems();
        List<JpaCategory> subcategories = jpaCategory.getSubcategories();
        while (!subcategories.isEmpty()) {
            List<JpaCategory> nextSubcategories = new ArrayList<>();
            for (JpaCategory subcategory : subcategories) {
                jpaItems.addAll(subcategory.getItems());
                if (subcategory.getSubcategories() != null)
                    nextSubcategories.addAll(subcategory.getSubcategories());
            }
            subcategories = nextSubcategories;
        }
        Type itemsListType = new TypeToken<List<Item>>() {
        }.getType();
        List<Item> items = modelMapper.map(jpaItems, itemsListType);

        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (!item.getOwner().getUsername().equals(currentUser))
                continue;
            if (fromPrice != null && item.getPrice().compareTo(fromPrice) < 0)
                continue;
            if (toPrice != null && item.getPrice().compareTo(toPrice) > 0)
                continue;

            List<Image> newItemImages = new ArrayList<>();
            for (Image image : item.getImages()) {
                try {
                    image.setData(imageService.loadImageDataFromDisk(image));
                    newItemImages.add(image);
                } catch (Exception e) {
                    log.error("Failed to load image.");
                    e.printStackTrace();
                }
            }
            item.setImages(newItemImages);
            filteredItems.add(item);
        }

        return filteredItems;
    }

    public Item getItem(Long id) {
        JpaItem jpaItem = itemRepository.findById(id).orElseThrow(() -> new NullPointerException("Item not found."));
        Item item = modelMapper.map(jpaItem, Item.class);

        List<Image> loadedImages = new ArrayList<>();
        for (Image image : item.getImages()) {
            try {
                image.setData(imageService.loadImageDataFromDisk(image));
            } catch (Exception e) {
                log.error("Failed to load image for item.", e);
            }
            loadedImages.add(image);
        }
        item.setImages(loadedImages);

        return item;
    }

}
