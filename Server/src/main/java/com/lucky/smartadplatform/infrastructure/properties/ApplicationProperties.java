package com.lucky.smartadplatform.infrastructure.properties;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("application")
@Validated
@Getter
@Setter
public class ApplicationProperties {

    @Valid
    @NotNull
    private List<String> itemImagesPath;

}
