package com.lucky.smartadplatform.domain.service.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import com.lucky.smartadplatform.application.rest.model.PredictionResult;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SentisightRecognitionApiServiceIntegrationTests {

    @Autowired
    private SentisightRecognitionApiService sentisightRecognitionApiService;

    @Test
    void testRecognizeUrlSuccess() {
        var expected = new ArrayList<PredictionResult>();
        expected.add(new PredictionResult("jersey, T-shirt, tee shirt", 87.3));
        expected.add(new PredictionResult("maillot", 0.5));
        expected.add(new PredictionResult("sweatshirt", 0.4));

        String imageUrl = "https://static.zajo.net/content/mediagallery/zajo_dcat/image/product/types/X/9088.png";
        var actual = sentisightRecognitionApiService.recognizeUrl(imageUrl);

        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

}
