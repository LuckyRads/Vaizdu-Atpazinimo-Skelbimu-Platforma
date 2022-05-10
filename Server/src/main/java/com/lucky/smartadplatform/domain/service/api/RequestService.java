package com.lucky.smartadplatform.domain.service.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.lucky.smartadplatform.domain.exception.ExternalRequestException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Class responsible for external request sending, parsing etc.
 * 
 * @author lucky
 */
@Service
@Slf4j
public class RequestService {

	private static final List<Integer> SUCCESS_HTTP_STATUS_CODES = new ArrayList<>(Arrays.asList(200, 201));

	/**
	 * Function to send external requests to various API services, like
	 * SentiSight.ai.
	 * 
	 * @param <T>     type for the response object to be cast to
	 * @param request
	 * @param type    to cast the response to
	 * @return parsed object of type {@code T}
	 * @throws IOException
	 */
	public <T> T send(HttpRequestBase request, Type type) throws IOException {
		T parsedResponse = null;
		CloseableHttpClient client = HttpClients.createDefault();

		try (CloseableHttpResponse requestResponse = client.execute(request)) {
			if (!SUCCESS_HTTP_STATUS_CODES.contains(requestResponse.getStatusLine().getStatusCode())) {
				StringBuilder exceptionMessageBuilder = new StringBuilder();
				exceptionMessageBuilder.append("External request failed with status code: ")
						.append(requestResponse.getStatusLine().getStatusCode()).append("\n")
						.append("External request message: ")
						.append(EntityUtils.toString(requestResponse.getEntity()));

				throw new ExternalRequestException(exceptionMessageBuilder.toString());
			}

			parsedResponse = parseResponse(requestResponse, type);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			client.close();
		}
		return parsedResponse;
	}

	/**
	 * Parses the response entity and maps it to a specified type.
	 * 
	 * @param httpResponse
	 * @param type         for the response object to be cast to
	 * @return parsed response that is cast to a specified object
	 */
	private <T> T parseResponse(CloseableHttpResponse httpResponse, Type type) {
		String jsonString = null;
		try {
			jsonString = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			log.error("Error parsing an external request response entity!");
			e.printStackTrace();
			return null;
		}
		try {
			return new Gson().fromJson(jsonString, type);
		} catch (JsonParseException e) {
			log.error(String.format("Could not cast response body to a specified type: %s", type.toString()));
			e.printStackTrace();
			return null;
		}
	}

}
