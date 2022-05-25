package com.lucky.smartadplatform.application.rest.model;

import java.util.Base64;

import com.lucky.smartadplatform.domain.Image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private Long id;

    private String name;

    private String data;

    private String category;

    private Double predictionConfidence;

    private String owner;

    private Long item;

    public ImageDto(Image image) {
        this.id = image.getId();
        this.name = image.getName();
        if (image.getData() != null)
            this.data = Base64.getEncoder().encodeToString(image.getData());
        this.category = image.getCategory();
        this.predictionConfidence = image.getPredictionConfidence();
        if (image.getOwner() != null)
            this.owner = image.getOwner().getUsername();
        if (image.getItem() != null)
            this.item = image.getItem().getId();
    }

}