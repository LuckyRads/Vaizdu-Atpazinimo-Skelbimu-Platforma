package com.lucky.smartadplatform.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lucky.smartadplatform.application.rest.model.PredictionResult;
import com.lucky.smartadplatform.domain.type.RoleType;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaCategory;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaImage;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaItem;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaRole;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;

public class TestUtils {

    public static JpaRole getUserRole() {
        return new JpaRole(RoleType.ROLE_USER);
    }

    public static JpaRole getAdminRole() {
        return new JpaRole(RoleType.ROLE_ADMIN);
    }

    public static JpaUser getTestUser1(Set<JpaRole> userRoles) {
        return new JpaUser(1L, "MatasM", "matas@vgtu.lt",
                "$2a$10$WyYBGdQVOpaSe/M8H0wl..QKyVGtQiXez4Cy6oINSmLlvV9CnkYLS", userRoles, new ArrayList<>(),
                new ArrayList<>());
    }

    public static JpaUser getTestUser2(Set<JpaRole> userRoles) {
        return new JpaUser(1L, "MatasTester", "matastester@vgtu.lt",
                "$2a$10$WyYBGdQVOpaSe/M8H0wl..QKyVGtQiXez4Cy6oINSmLlvV9CnkYLS", userRoles, new ArrayList<>(),
                new ArrayList<>());
    }

    public static JpaCategory getTestCategory1() {
        return new JpaCategory(1L, "All", new ArrayList<>(), null, new ArrayList<>());
    }

    public static JpaCategory getTestCategory2(JpaCategory parentCategory) {
        return new JpaCategory(2L, "Clothing", new ArrayList<>(), parentCategory, new ArrayList<>());
    }

    public static JpaCategory getTestCategory3(JpaCategory parentCategory) {
        return new JpaCategory(3L, "Clothing tops", new ArrayList<>(), parentCategory,
                new ArrayList<>());
    }

    public static JpaImage getTestImage1(JpaUser owner) {
        return new JpaImage(1L, "tshirt.jpg", "Clothing tops", owner, null);
    }

    public static JpaImage getTestImage2(JpaUser owner) {
        return new JpaImage(2L, "watch.jpg", "Clothing", owner, null);
    }

    public static JpaItem getTestItem1(JpaUser owner, JpaCategory category) {
        Long id = 1L;
        String name = "T-shirt";
        BigDecimal price = new BigDecimal("15");
        String description = "Good condition.";
        String contactNumber = "+37061231231";

        List<JpaImage> itemImages = new ArrayList<>();
        itemImages.add(getTestImage1(owner));

        JpaItem item = new JpaItem(id, name, description, price, contactNumber, itemImages, owner,
                category);
        itemImages.forEach(itemImage -> itemImage.setItem(item));
        item.setCategory(category);
        category.addItem(item);
        return item;
    }

    public static JpaItem getTestItem2(JpaUser owner, JpaCategory category) {
        Long id = 2L;
        String name = "Watch";
        BigDecimal price = new BigDecimal("25");
        String description = "Watch is in excellent condition";
        String contactNumber = "+37061111111";

        List<JpaImage> itemImages = new ArrayList<>();
        itemImages.add(getTestImage2(owner));

        JpaItem item = new JpaItem(id, name, description, price, contactNumber, itemImages, owner,
                category);
        itemImages.forEach(itemImage -> itemImage.setItem(item));
        item.setCategory(category);
        category.addItem(item);
        return item;
    }

    public static PredictionResult getTestPredictionResult1() {
        return new PredictionResult("jersey, T-shirt, tee shirt, score", Double.valueOf(85));
    }

}
