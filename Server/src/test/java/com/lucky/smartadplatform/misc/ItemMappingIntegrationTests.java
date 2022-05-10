package com.lucky.smartadplatform.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.lucky.smartadplatform.domain.Category;
import com.lucky.smartadplatform.domain.Item;
import com.lucky.smartadplatform.domain.User;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaCategory;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaItem;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ItemMappingIntegrationTests {

        @Autowired
        private ModelMapper modelMapper;

        @Test
        void testMapToDomainLinkedObjectsSuccess() {
                var user = new JpaUser("tester@mail.com", "hashedPw");
                var rootItems = new ArrayList<JpaItem>();
                rootItems.add(JpaItem.builder().name("Root thing")
                                .description("root testing object.").owner(user).build());
                user.setItems(rootItems);

                var rootCategory = JpaCategory.builder().name("Root").items(rootItems).build();
                var subcategories = new ArrayList<JpaCategory>();
                subcategories.add(JpaCategory.builder().name("Subcategory").parentCategory(rootCategory).build());
                rootCategory.setSubcategories(subcategories);

                var expectedItem = JpaItem.builder().name("Item name").description("Main item for testing.")
                                .category(rootCategory)
                                .owner(user).build();
                var actualItem = modelMapper.map(expectedItem, Item.class);

                assertEquals(expectedItem.getName(), actualItem.getName());
                assertEquals(expectedItem.getCategory().getName(), actualItem.getCategory().getName());
                assertEquals(expectedItem.getCategory().getSubcategories().get(0).getName(),
                                actualItem.getCategory().getSubcategories().get(0).getName());
                assertEquals(expectedItem.getOwner().getEmail(), actualItem.getOwner().getEmail());
        }

        @Test
        void testMapToEntityLinkedObjectsSuccess() {
                var user = new User("Supername", "tester@mail.com", "hashedPw", new ArrayList<>(), null);
                var rootItems = new ArrayList<Item>();
                rootItems.add(new Item("Root thing", BigDecimal.ONE, "root testing object.", "+3706123123", null, null,
                                null));
                user.setItems(rootItems);

                var rootCategory = new Category("Root", null, null, rootItems);
                var subcategories = new ArrayList<Category>();
                subcategories.add(new Category("Subcategory", null, rootCategory, null));
                rootCategory.setSubcategories(subcategories);

                var expectedItem = new Item("Item name", BigDecimal.ONE, "Main item for testing", "+3706123123", null,
                                rootCategory,
                                user);
                var actualItem = modelMapper.map(expectedItem, JpaItem.class);

                assertEquals(expectedItem.getName(), actualItem.getName());
                assertEquals(expectedItem.getCategory().getName(), actualItem.getCategory().getName());
                assertEquals(expectedItem.getCategory().getSubcategories().get(0).getName(),
                                actualItem.getCategory().getSubcategories().get(0).getName());
                assertEquals(expectedItem.getOwner().getEmail(), actualItem.getOwner().getEmail());
        }

}
