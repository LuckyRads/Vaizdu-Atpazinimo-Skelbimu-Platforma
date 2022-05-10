package com.lucky.smartadplatform.domain.service.api;

import java.util.List;

import com.lucky.smartadplatform.application.rest.model.PredictionResult;

/**
 * Interface for image recognition API services.
 * 
 * @author lucky
 */
public interface RecognitionApiService {

	/**
	 * Send a recognition request using an already uploaded image from the internet.
	 * 
	 * @param imageUrl
	 * @return list of prediction results
	 */
	public List<PredictionResult> recognizeUrl(String imageUrl);

	/**
	 * Send a recognition request with an image, that is encoded to Base64.
	 * 
	 * @param base64Image
	 * @return list of prediction results
	 */
	public List<PredictionResult> recognizeBase64(String base64Image);

}
