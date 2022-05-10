package com.lucky.smartadplatform.domain.service.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lucky.smartadplatform.application.rest.model.PredictionResult;
import com.lucky.smartadplatform.domain.factory.SentisightRequestFactory;
import com.lucky.smartadplatform.infrastructure.properties.SentisightProperties;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * SentiSight.ai pre-trained recognition model API service implementation.
 * 
 * @author lucky
 */
@Service
@Slf4j
public class SentisightRecognitionApiService implements RecognitionApiService {

	private static final String PRETRAINED_MODEL = "General-Classification";

	private final SentisightRequestFactory sentisightRequestFactory;

	private final RequestService requestService;

	private final String serviceBaseUrl;

	public SentisightRecognitionApiService(SentisightProperties sentisightProperties,
			SentisightRequestFactory sentisightRequestFactory,
			RequestService requestService) {
		super();
		this.sentisightRequestFactory = sentisightRequestFactory;
		this.requestService = requestService;
		this.serviceBaseUrl = new StringBuilder().append(sentisightProperties.getBaseUrl()).append("/api/")
				.append(sentisightProperties.isCustomModel() ? "predict/" : "pm-predict/")
				.append(sentisightProperties.isCustomModel() ? sentisightProperties.getProjectId() + "/" : "")
				.append(sentisightProperties.isCustomModel() ? sentisightProperties.getModelName()
						: PRETRAINED_MODEL)
				.toString();
	}

	@Override
	public List<PredictionResult> recognizeUrl(String imageUrl) {
		try {
			URI uri = new URI(serviceBaseUrl);
			HttpPost request = sentisightRequestFactory.getSentisightPostRequest(uri);

			JsonObject json = new JsonObject();
			json.addProperty("url", imageUrl);
			request.setEntity(new StringEntity(json.toString()));

			Type type = new TypeToken<ArrayList<PredictionResult>>() {
			}.getType();
			return requestService.send(request, type);
		} catch (URISyntaxException e) {
			log.error("Malformed SentiSight request URL!");
			e.printStackTrace();
			return Collections.emptyList();
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			log.error("Error constructing HTTP entity!");
			e.printStackTrace();
			return Collections.emptyList();
		} catch (IOException | IllegalStateException e) {
			log.error("External request failed! Aborting further operations.");
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public List<PredictionResult> recognizeBase64(String base64Image) {
		try {
			URI uri = new URI(serviceBaseUrl);
			HttpPost request = sentisightRequestFactory.getSentisightPostRequest(uri);

			JsonObject json = new JsonObject();
			json.addProperty("base64", base64Image);
			request.setEntity(new StringEntity(json.toString()));

			Type type = new TypeToken<ArrayList<PredictionResult>>() {
			}.getType();
			return requestService.send(request, type);
		} catch (URISyntaxException e) {
			log.error("Malformed SentiSight request URL!");
			e.printStackTrace();
			return Collections.emptyList();
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			log.error("Error constructing HTTP entity!");
			e.printStackTrace();
			return Collections.emptyList();
		} catch (IOException | IllegalStateException e) {
			log.error("External request failed! Aborting further operations.");
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

}
