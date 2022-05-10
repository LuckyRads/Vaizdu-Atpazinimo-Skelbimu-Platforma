package com.lucky.smartadplatform.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.lucky.smartadplatform.application.rest.model.PredictionResult;
import com.lucky.smartadplatform.domain.Image;
import com.lucky.smartadplatform.domain.repository.ImageRepository;
import com.lucky.smartadplatform.domain.repository.UserRepository;
import com.lucky.smartadplatform.domain.service.api.RecognitionApiService;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaCategory;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaImage;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaItem;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaRole;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;
import com.lucky.smartadplatform.infrastructure.service.RecognitionServiceUtil;
import com.lucky.smartadplatform.util.TestUtils;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ImageServiceUnitTests {

    private ImageService imageService;

    @Mock
    private UserRepository<JpaUser> userRepository;

    @Mock
    private ImageRepository<JpaImage> imageRepository;

    @Mock
    private FileService fileService;

    @Mock
    private RecognitionApiService recognitionApiService;

    @Mock
    private RecognitionServiceUtil recognitionServiceUtil;

    @Captor
    private ArgumentCaptor<JpaImage> jpaImageCaptor;

    private JpaUser user1;

    private JpaCategory categoryAll;
    private JpaCategory categoryClothing;
    private JpaCategory categoryClothingTops;

    @BeforeEach
    public void setUp() {
        imageService = new ImageService(userRepository, imageRepository, fileService, new ModelMapper(),
                recognitionApiService, recognitionServiceUtil);
    }

    private void prepareUsers() {
        Set<JpaRole> userRoles = new HashSet<>();
        userRoles.add(TestUtils.getUserRole());
        user1 = TestUtils.getTestUser1(userRoles);
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

    @Test
    void testSaveImageValidSuccess() throws NullPointerException, IOException {
        prepareUsers();

        JpaUser imageOwner = user1;

        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = new MockMultipartFile("tshirt.jpg", "tshirt.jpg", "image/jpeg", fileInputStream);

        Mockito.when(userRepository.findByUsername(imageOwner.getUsername())).thenReturn(Optional.of(imageOwner));
        Mockito.doNothing().when(fileService).saveImageToDisk(any(), eq(multipartFile));

        Image image = imageService.saveImage(multipartFile, imageOwner.getUsername());

        assertEquals("tshirt.jpg", image.getName());
    }

    @Test
    void testDeleteImageValidSuccess() throws IOException {
        prepareUsers();

        JpaUser imageOwner = user1;
        JpaImage image = TestUtils.getTestImage1(imageOwner);

        Long id = 1L;

        Mockito.when(imageRepository.findById(id)).thenReturn(Optional.of(image));
        MockedStatic<FileUtils> fileUtils = Mockito.mockStatic(FileUtils.class);
        fileUtils.when(() -> FileUtils.delete(any())).thenReturn(null);

        Image deletedImage = imageService.deleteImage(id, imageOwner.getUsername());

        assertEquals(image.getId(), deletedImage.getId());
        Mockito.verify(imageRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void testGetUploadedItemImagesTwoSuccess() {
        prepareUsers();
        prepareCategories();

        JpaUser owner = user1;
        JpaItem item = TestUtils.getTestItem1(owner, categoryClothingTops);
        JpaImage tempImage = TestUtils.getTestImage2(owner);
        item.getImages().forEach(image -> owner.addImage(image));
        owner.addImage(tempImage);

        List<Long> imageIds = new ArrayList<>(Arrays.asList(1L, 2L));

        Mockito.when(imageRepository.findUserImages(owner.getUsername())).thenReturn(owner.getImages());

        List<JpaImage> images = imageService.getUploadedItemImages(owner.getUsername(), imageIds);

        assertEquals(tempImage.getId(), images.get(0).getId());
    }

    @Test
    void testGetUserTempImagesOneSuccess() {
        prepareUsers();
        prepareCategories();

        JpaUser owner = user1;
        JpaItem item = TestUtils.getTestItem1(owner, categoryClothingTops);
        JpaImage tempImage = TestUtils.getTestImage2(owner);

        item.getImages().forEach(image -> owner.addImage(image));
        owner.addImage(tempImage);

        Mockito.when(imageRepository.findUserImages(owner.getUsername())).thenReturn(owner.getImages());

        List<JpaImage> images = imageService.getUserTempImages(owner.getUsername());

        assertEquals(tempImage.getId(), images.get(0).getId());

    }

    @Test
    void testPredictCategoryRecognized() {
        prepareUsers();
        prepareCategories();

        JpaUser owner = user1;
        JpaImage jpaImage = TestUtils.getTestImage1(owner);

        Image image = new ModelMapper().map(jpaImage, Image.class);
        image.setData(Base64.getDecoder().decode("testimgdata"));
        PredictionResult predictionResult1 = TestUtils.getTestPredictionResult1();

        Mockito.when(recognitionApiService.recognizeBase64(any())).thenReturn(Arrays.asList(predictionResult1));
        Mockito.when(recognitionServiceUtil.getCategoryNameFromPredicted(predictionResult1.getLabel()))
                .thenReturn("Clothing tops");
        Mockito.when(imageRepository.findById(image.getId())).thenReturn(Optional.of(jpaImage));

        imageService.predictCategory(image);

        Mockito.verify(imageRepository, Mockito.times(1)).save(jpaImageCaptor.capture());
    }

}
