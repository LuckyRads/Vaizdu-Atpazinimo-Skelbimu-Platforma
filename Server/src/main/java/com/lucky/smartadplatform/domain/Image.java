package com.lucky.smartadplatform.domain;

import com.lucky.smartadplatform.domain.type.ImageDirectoryType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Image {

    private Long id;

    private String name;

    private byte[] data;

    private String category;

    private Double predictionConfidence;

    private User owner;

    private Item item;

    public Image(String name, byte[] data, User owner) {
        this.name = name;
        this.data = data;
        this.owner = owner;
    }

    public ImageDirectoryType getImageDirectoryType() {
        if (item == null && owner != null) {
            return ImageDirectoryType.USER_TEMP;
        }
        if (item != null && owner != null) {
            return ImageDirectoryType.USER_ITEM;
        }
        return ImageDirectoryType.TEMP;
    }

    public String getFilename() {
        return this.id + ".png";
    }

}
