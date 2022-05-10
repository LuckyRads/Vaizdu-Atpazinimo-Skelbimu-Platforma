package com.lucky.smartadplatform;

import com.lucky.smartadplatform.infrastructure.configuration.JpaConfiguration;
import com.lucky.smartadplatform.infrastructure.properties.ApplicationProperties;
import com.lucky.smartadplatform.infrastructure.properties.SentisightProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan
@EnableConfigurationProperties({ SentisightProperties.class, ApplicationProperties.class })
@Import(JpaConfiguration.class)
public class SmartAdPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartAdPlatformApplication.class, args);
	}

}
