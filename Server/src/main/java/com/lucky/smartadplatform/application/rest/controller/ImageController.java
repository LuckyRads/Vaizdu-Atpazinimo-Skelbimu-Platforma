package com.lucky.smartadplatform.application.rest.controller;

import java.io.IOException;

import com.lucky.smartadplatform.application.rest.model.GenericResponse;
import com.lucky.smartadplatform.application.rest.model.ImageDto;
import com.lucky.smartadplatform.application.rest.model.StatusResponse;
import com.lucky.smartadplatform.application.rest.model.StatusResponse.StatusCode;
import com.lucky.smartadplatform.domain.Image;
import com.lucky.smartadplatform.domain.service.ImageService;
import com.lucky.smartadplatform.infrastructure.security.service.AuthUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<GenericResponse<ImageDto>> uploadImage(@RequestBody MultipartFile imageFile) {
        var response = new GenericResponse<ImageDto>();

        String username = AuthUtil.getCurrentUsername();
        Image image = null;
        try {
            image = imageService.saveImage(imageFile, username);
        } catch (IOException e) {
            response.setStatus(StatusCode.FAILED);
            response.setMessage("Invalid image.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageService.predictCategory(image);

        ImageDto imageDto = new ImageDto(image);
        response.setData(imageDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusResponse> deleteImage(@PathVariable("id") Long id) {
        var response = new StatusResponse();

        String username = AuthUtil.getCurrentUsername();

        try {
            imageService.deleteImage(id, username);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(StatusCode.FAILED);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.setMessage("Image deleted.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
