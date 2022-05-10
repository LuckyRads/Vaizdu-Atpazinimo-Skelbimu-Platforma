package com.lucky.smartadplatform.infrastructure.properties;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("security")
@Validated
@Getter
@Setter
public class SecurityProperties {

    @Valid
    @NotNull
    private String jwtSecret;

    @Valid
    @NotNull
    private long jwtExpirationTimeMs;

}
