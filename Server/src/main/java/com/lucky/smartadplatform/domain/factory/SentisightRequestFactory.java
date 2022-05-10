package com.lucky.smartadplatform.domain.factory;

import java.net.URI;

import com.lucky.smartadplatform.infrastructure.properties.SentisightProperties;

import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

/**
 * Factory to create various HTTP request for SentiSight image recognition
 * service.
 * 
 * @author lucky
 */
@Component
public class SentisightRequestFactory {

    private final SentisightProperties sentisightProperties;

    public SentisightRequestFactory(SentisightProperties sentisightProperties) {
        this.sentisightProperties = sentisightProperties;
    }

    /**
     * @param uri
     * @return authenticated SentiSight.ai {@code HttpPost} request
     */
    public HttpPost getSentisightPostRequest(URI uri) {
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("X-Auth-Token", sentisightProperties.getApiToken());
        return request;
    }

}
