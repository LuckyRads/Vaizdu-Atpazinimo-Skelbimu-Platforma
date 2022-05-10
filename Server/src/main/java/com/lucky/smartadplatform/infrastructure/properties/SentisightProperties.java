package com.lucky.smartadplatform.infrastructure.properties;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for image recognition API (SentiSight.ai). For more
 * information about the API usage - check out
 * {@link https://www.sentisight.ai/user-guide}.
 * 
 * @author lucky
 */
@ConfigurationProperties("sentisight")
@Validated
@Getter
@Setter
public class SentisightProperties {

	@Valid
	@NotNull
	private String baseUrl;

	@Valid
	@NotNull
	private String apiToken;

	@Valid
	@NotNull
	private boolean customModel;

	@Valid
	@NotNull
	private String projectId;

	@Valid
	@NotNull
	private String modelName;

	@Valid
	@NotNull
	private List<String> recognitionClassFilePath;

}
