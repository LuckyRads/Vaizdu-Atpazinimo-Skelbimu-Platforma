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

    private User owner;

    private Item item;

    public Image(String name, byte[] data, User owner) {
        this.name = name;
        this.data = data;
        this.owner = owner;
    }

    // public void setGeneratedName(String name) {
    // if (name != null) {
    // List<String> splitName = Arrays.asList(name.split(Pattern.quote(".")));
    // if (splitName.size() > 1) {
    // this.name = String.join("", splitName.subList(0, splitName.size() - 1)) + "-"
    // + UUID.randomUUID().toString().substring(0, 5);
    // } else {
    // this.name = name + "-" + UUID.randomUUID().toString().substring(0, 5);
    // }
    // }
    // }

    // public void regenerateName() {
    // if (this.name != null) {
    // this.name = this.name.substring(0, this.name.length() - 5);
    // setGeneratedName(name);
    // }
    // }

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
