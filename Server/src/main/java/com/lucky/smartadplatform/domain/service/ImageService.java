package com.lucky.smartadplatform.domain.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.lucky.smartadplatform.application.rest.model.PredictionResult;
import com.lucky.smartadplatform.domain.Image;
import com.lucky.smartadplatform.domain.User;
import com.lucky.smartadplatform.domain.repository.ImageRepository;
import com.lucky.smartadplatform.domain.repository.UserRepository;
import com.lucky.smartadplatform.domain.service.api.RecognitionApiService;
import com.lucky.smartadplatform.domain.type.ImageDirectoryType;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaImage;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;
import com.lucky.smartadplatform.infrastructure.service.RecognitionServiceUtil;

import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ImageService {

    @Autowired
    private UserRepository<JpaUser> userRepository;

    @Autowired
    private ImageRepository<JpaImage> imageRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RecognitionApiService recognitionApiService;

    @Autowired
    private RecognitionServiceUtil recognitionServiceUtil;

    public Image saveImage(MultipartFile imageFile, String owner) throws IOException, NullPointerException {
        JpaUser jpaUser = userRepository.findByUsername(owner).orElseThrow(NullPointerException::new);
        User user = modelMapper.map(jpaUser, User.class);

        Image image = new Image(imageFile.getOriginalFilename(), imageFile.getBytes(), user);

        JpaImage jpaImage = modelMapper.map(image, JpaImage.class);
        jpaImage.setOwner(jpaUser);
        imageRepository.save(jpaImage);
        image.setId(jpaImage.getId());
        fileService.saveImageToDisk(image, imageFile);

        return image;
    }

    public void predictCategory(Image image) {
        List<PredictionResult> predictionResults = recognitionApiService
                .recognizeBase64(Base64.getEncoder().encodeToString(image.getData()));
        if (!predictionResults.isEmpty()) {
            String categoryString = recognitionServiceUtil
                    .getCategoryNameFromPredicted(predictionResults.get(0).getLabel());
            image.setCategory(categoryString);
            Optional<JpaImage> optionalJpaImage = imageRepository.findById(image.getId());
            if (optionalJpaImage.isPresent()) {
                JpaImage jpaImage = optionalJpaImage.get();
                jpaImage.setCategory(categoryString);
                imageRepository.save(jpaImage);
            }
        }
    }

    public void moveImageFromTempToUserItem(Image image) throws IOException {
        File tempImgFile = new File(fileService.getUserTempImagesDir(image.getOwner()) + image.getFilename());
        File itemImgFile = new File(fileService.getUserItemImagesDir(image.getOwner(), image.getItem()));
        FileUtils.copyFileToDirectory(tempImgFile, itemImgFile);
    }

    public Image deleteImage(Long id, String currentUser) throws IOException {
        JpaImage jpaImage = imageRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Image not found."));

        if (!jpaImage.getOwner().getUsername().equals(currentUser))
            throw new IllegalAccessError("Unauthorized!");

        Image image = modelMapper.map(jpaImage, Image.class);
        String imagePath = null;
        if (image.getImageDirectoryType().equals(ImageDirectoryType.USER_TEMP)) {
            imagePath = fileService.getUserTempImagesDir(image.getOwner());
        } else if (image.getImageDirectoryType().equals(ImageDirectoryType.USER_ITEM)) {
            imagePath = fileService.getUserItemImagesDir(image.getOwner(), image.getItem());
        }
        File file = new File(imagePath + image.getFilename());
        FileUtils.delete(file);

        imageRepository.deleteById(id);

        return image;
    }

    public byte[] loadImageDataFromDisk(Image image) throws IOException {
        String imagePath = null;
        if (image.getImageDirectoryType().equals(ImageDirectoryType.USER_TEMP)) {
            imagePath = fileService.getUserTempImagesDir(image.getOwner());
        } else if (image.getImageDirectoryType().equals(ImageDirectoryType.USER_ITEM)) {
            imagePath = fileService.getUserItemImagesDir(image.getOwner(), image.getItem());
        }
        File file = new File(imagePath + image.getFilename());
        BufferedImage bufferedImage = ImageIO.read(file);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public List<JpaImage> getUserTempImages(String ownerName) {
        List<JpaImage> jpaTempImages = imageRepository.findUserImages(ownerName);
        return jpaTempImages.stream().filter(image -> image.getItem() == null).collect(Collectors.toList());
    }

    public List<JpaImage> getUploadedItemImages(String ownerName, List<Long> imageIds) {
        List<JpaImage> jpaTempImages = getUserTempImages(ownerName);
        return jpaTempImages.stream()
                .filter(image -> imageIds.stream().anyMatch(imageId -> imageId.equals(image.getId())))
                .collect(Collectors.toList());
    }

}
