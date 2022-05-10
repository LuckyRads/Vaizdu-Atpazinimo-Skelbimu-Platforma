package com.lucky.smartadplatform.infrastructure.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.lucky.smartadplatform.infrastructure.model.jpa")
@EnableJpaRepositories("com.lucky.smartadplatform.infrastructure.repository.jpa")
public class JpaConfiguration {

}
