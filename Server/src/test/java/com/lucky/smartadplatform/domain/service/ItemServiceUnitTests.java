package com.lucky.smartadplatform.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.lucky.smartadplatform.application.rest.model.ImageDto;
import com.lucky.smartadplatform.application.rest.model.ItemDto;
import com.lucky.smartadplatform.domain.Item;
import com.lucky.smartadplatform.domain.repository.CategoryRepository;
import com.lucky.smartadplatform.domain.repository.ImageRepository;
import com.lucky.smartadplatform.domain.repository.ItemRepository;
import com.lucky.smartadplatform.domain.repository.UserRepository;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaCategory;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaImage;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaItem;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaRole;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;
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
public class ItemServiceUnitTests {

    private ItemService itemService;

    @Mock
    private ImageService imageService;

    @Mock
    private ItemRepository<JpaItem> itemRepository;

    @Mock
    private CategoryRepository<JpaCategory> categoryRepository;

    @Mock
    private UserRepository<JpaUser> userRepository;

    @Mock
    private ImageRepository<JpaImage> imageRepository;

    @Captor
    private ArgumentCaptor<JpaItem> jpaItemArgumentCaptor;

    private JpaCategory categoryAll;
    private JpaCategory categoryClothing;
    private JpaCategory categoryClothingTops;

    private JpaUser user1;
    private JpaUser user2;

    @BeforeEach
    public void setUp() {
        itemService = new ItemService(new ModelMapper(), imageService, itemRepository, categoryRepository,
                userRepository,
                imageRepository);
    }

    private void prepareCategories() {
        categoryAll = null;
        categoryClothing = null;
        categoryClothingTops = null;

        categoryAll = TestUtils.getTestCategory1();
        categoryClothing = TestUtils.getTestCategory2(categoryAll);
        categoryClothingTops = TestUtils.getTestCategory3(categoryClothing);
        categoryAll.setSubcategories(new ArrayList<>(Arrays.asList(categoryClothing)));
        categoryClothing.setSubcategories(new ArrayList<>(Arrays.asList(categoryClothingTops)));
    }

    private void prepareUsers() {
        user1 = null;
        user2 = null;

        Set<JpaRole> userRoles = new HashSet<>();
        userRoles.add(TestUtils.getUserRole());

        user1 = TestUtils.getTestUser1(userRoles);
        user2 = TestUtils.getTestUser2(userRoles);
    }

    private JpaItem getPreparedItem1(JpaUser itemOwner, JpaCategory category) {
        return TestUtils.getTestItem1(itemOwner, category);
    }

    private JpaItem getPreparedItem2(JpaUser itemOwner, JpaCategory category) {
        return TestUtils.getTestItem2(itemOwner, category);
    }

    @Test
    void testCreateItemValidSuccess() throws IOException {
        prepareCategories();
        prepareUsers();

        JpaUser userOwner = user1;
        JpaCategory itemCategory = categoryClothingTops;

        String name = "T-shirt";
        BigDecimal price = new BigDecimal("15");
        String description = "Good condition.";
        String contactNumber = "+37061231231";
        String categoryName = "Clothing tops";
        String ownerName = "MatasM";
        List<Long> imageIds = new ArrayList<>(Arrays.asList(1L));

        List<JpaImage> userImages = new ArrayList<>();
        userImages.add(new JpaImage(1L, "tshirt.jpg", "Clothing tops", 98.9, userOwner, null));

        Mockito.when(categoryRepository.findByName("Clothing tops")).thenReturn(Optional.of(itemCategory));
        Mockito.when(userRepository.findByUsername(ownerName)).thenReturn(Optional.of(userOwner));
        Mockito.when(imageService.getUploadedItemImages(ownerName, imageIds)).thenReturn(userImages);

        Item createdItem = itemService.createItem(name, price, description, contactNumber, categoryName, ownerName,
                imageIds);

        assertEquals(name, createdItem.getName());
        assertEquals(categoryName, createdItem.getCategory().getName());
        assertEquals(userImages.get(0).getName(), createdItem.getImages().get(0).getName());

        Mockito.verify(itemRepository, Mockito.times(1)).save(jpaItemArgumentCaptor.capture());
    }

    @Test
    void testUpdateItemValidSuccess() throws IOException {
        prepareCategories();
        prepareUsers();
        JpaUser itemOwner = user1;

        JpaCategory itemCategory = categoryClothingTops;
        JpaItem itemToBeUpdated = getPreparedItem1(itemOwner, itemCategory);

        itemOwner.setImages(itemToBeUpdated.getImages());

        Long id = 1L;
        List<Long> imageIds = new ArrayList<>(Arrays.asList(1L));
        String ownerName = "MatasM";
        String updatedName = "Mod t-shirt";
        BigDecimal updatedPrice = new BigDecimal("20");
        String updatedDesc = "Excellent condition.";
        String updatedContNum = "+37061111111";
        String updatedCategory = "Clothing tops";

        List<ImageDto> updatedImages = new ArrayList<>();
        Long updatedImageId = 1L;
        String updatedImageName = "tshirt.jpg";
        String updatedImageCategory = "Clothing tops";
        updatedImages
                .add(new ImageDto(updatedImageId, updatedImageName, null, updatedImageCategory, 94.1, ownerName, id));

        ItemDto itemDto = new ItemDto(id, updatedName, updatedPrice, updatedDesc, updatedContNum, updatedImages,
                updatedCategory, ownerName);

        Mockito.when(itemRepository.findById(id)).thenReturn(Optional.of(itemToBeUpdated));
        Mockito.when(categoryRepository.findByName("Clothing tops")).thenReturn(Optional.of(itemCategory));
        Mockito.when(imageService.getUploadedItemImages(ownerName, imageIds)).thenReturn(itemOwner.getImages());

        Item updatedItem = itemService.updateItem(id, imageIds, itemDto, ownerName);

        assertEquals(updatedName, updatedItem.getName());
        assertEquals(updatedCategory, updatedItem.getCategory().getName());
        assertEquals(updatedImages.get(0).getName(), updatedItem.getImages().get(0).getName());
        Mockito.verify(itemRepository, Mockito.times(1)).save(jpaItemArgumentCaptor.capture());
    }

    @Test
    void testDeleteItemValidSuccess() {
        prepareCategories();
        prepareUsers();
        JpaUser itemOwner = user1;
        JpaCategory itemCategory = categoryClothingTops;
        JpaItem itemToBeDeleted = getPreparedItem1(itemOwner, itemCategory);
        itemOwner.setImages(itemToBeDeleted.getImages());

        Long id = 1L;
        String owner = "MatasM";

        Mockito.when(itemRepository.findById(id)).thenReturn(Optional.of(itemToBeDeleted));

        itemService.deleteItem(id, owner);

        Mockito.verify(itemRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void testGetItemsAllSuccess() {
        prepareCategories();
        prepareUsers();
        JpaUser item1Owner = user1;
        JpaUser item2Owner = user2;
        JpaItem item1 = getPreparedItem1(item1Owner, categoryClothingTops);
        JpaItem item2 = getPreparedItem2(item2Owner, categoryClothing);

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryAll));

        List<Item> retrievedItems = itemService.getItems(categoryAll.getId());

        assertEquals(retrievedItems.get(1).getName(), item1.getName());
        assertEquals(retrievedItems.get(0).getName(), item2.getName());
        assertEquals(retrievedItems.get(1).getCategory().getName(), categoryClothingTops.getName());
        assertEquals(retrievedItems.get(0).getCategory().getName(), categoryClothing.getName());
        assertEquals(retrievedItems.get(1).getOwner().getUsername(), user1.getUsername());
        assertEquals(retrievedItems.get(0).getOwner().getUsername(), user2.getUsername());
        assertEquals("tshirt.jpg", retrievedItems.get(1).getImages().get(0).getName());
        assertEquals("watch.jpg", retrievedItems.get(0).getImages().get(0).getName());
    }

    @Test
    void testGetFilteredItemsLowerBoundarySuccess() {
        prepareCategories();
        prepareUsers();
        JpaUser item1Owner = user1;
        JpaItem item1 = getPreparedItem1(item1Owner, categoryClothingTops);

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryAll));

        List<Item> retrievedItems = itemService.getFilteredItems(categoryAll.getId(), new BigDecimal(15),
                new BigDecimal(20));

        assertEquals(1, retrievedItems.size());
        assertEquals(item1.getName(), retrievedItems.get(0).getName());
        assertEquals(item1.getOwner().getUsername(), retrievedItems.get(0).getOwner().getUsername());
        assertEquals("Clothing tops", retrievedItems.get(0).getCategory().getName());
        assertEquals("tshirt.jpg", retrievedItems.get(0).getImages().get(0).getName());
    }

    @Test
    void testGetOwnedItemsAllSuccess() {
        prepareCategories();
        prepareUsers();
        JpaUser item1Owner = user1;
        JpaItem item1 = getPreparedItem1(item1Owner, categoryClothingTops);

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryAll));

        List<Item> retrievedItems = itemService.getFilteredItems(categoryAll.getId(), new BigDecimal(15),
                new BigDecimal(20));

        assertEquals(1, retrievedItems.size());
        assertEquals(item1.getName(), retrievedItems.get(0).getName());
        assertEquals(item1.getOwner().getUsername(), retrievedItems.get(0).getOwner().getUsername());
        assertEquals("Clothing tops", retrievedItems.get(0).getCategory().getName());
        assertEquals("tshirt.jpg", retrievedItems.get(0).getImages().get(0).getName());
    }

    @Test
    void testGetItemExistingSuccess() {
        prepareCategories();
        prepareUsers();
        JpaUser itemOwner = user1;
        JpaItem item1 = getPreparedItem1(itemOwner, categoryClothingTops);

        Long id = 1L;

        Mockito.when(itemRepository.findById(id)).thenReturn(Optional.of(item1));

        Item item = itemService.getItem(id);

        assertEquals(item1.getName(), item.getName());
        assertEquals(item1.getCategory().getName(), item.getCategory().getName());
        assertEquals(item1.getOwner().getUsername(), item.getOwner().getUsername());
    }

}
