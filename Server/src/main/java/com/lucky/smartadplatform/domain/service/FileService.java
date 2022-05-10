package com.lucky.smartadplatform.domain.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.lucky.smartadplatform.domain.Image;
import com.lucky.smartadplatform.domain.Item;
import com.lucky.smartadplatform.domain.User;
import com.lucky.smartadplatform.domain.type.ImageDirectoryType;
import com.lucky.smartadplatform.infrastructure.properties.ApplicationProperties;
import com.lucky.smartadplatform.infrastructure.util.FileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Service
@Slf4j
@AllArgsConstructor
public class FileService {

    @Autowired
    private ApplicationProperties applicationProperties;

    public String getImagesDir() {
        return FileUtil.getDirPath(applicationProperties.getItemImagesPath());
    }

    public String getUserImagesDir(User user) {
        StringBuilder path = new StringBuilder();
        path.append(getImagesDir()).append(user.getUsername()).append(File.separator);
        return path.toString();
    }

    public String getUserTempImagesDir(User user) {
        StringBuilder path = new StringBuilder();
        path.append(getUserImagesDir(user)).append("temp").append(File.separator);
        return path.toString();
    }

    public String getUserItemImagesDir(User user, Item item) {
        StringBuilder path = new StringBuilder();
        path.append(getUserImagesDir(user)).append(item.getId()).append(File.separator);
        return path.toString();
    }

    public void saveImageToDisk(Image image, MultipartFile multipartImage) throws IOException {
        BufferedImage bufferedImage = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image.getData());) {
            bufferedImage = ImageIO.read(byteArrayInputStream);
        } catch (Exception e) {
            log.error("Could not read image from bytes: {}", e.getMessage());
            throw new IOException();
        }
        if (bufferedImage == null)
            throw new IOException();

        String fullImageDir = null;
        String fullImgPath = null;
        if (image.getImageDirectoryType().equals(ImageDirectoryType.USER_TEMP)) {
            fullImageDir = getUserTempImagesDir(image.getOwner());
        } else if (image.getImageDirectoryType().equals(ImageDirectoryType.USER_ITEM)) {
            fullImageDir = getUserItemImagesDir(image.getOwner(), image.getItem());
        }
        fullImgPath = fullImageDir + image.getFilename();

        File imageDirFile = new File(fullImageDir);
        if (!imageDirFile.exists()) {
            imageDirFile.mkdirs();
        }

        File imageFile = new File(fullImgPath);
        while (imageFile.exists()) {
            fullImgPath = fullImageDir + image.getFilename();
            imageFile = new File(fullImgPath);
        }

        ImageIO.write(
                Thumbnails.of(multipartImage.getInputStream()).size(512, 512).outputFormat("png").keepAspectRatio(true)
                        .asBufferedImage(),
                "png", new File(fullImgPath));
    }

}
