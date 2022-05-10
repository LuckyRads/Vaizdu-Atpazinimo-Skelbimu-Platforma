package com.lucky.smartadplatform.infrastructure.configuration;

import com.lucky.smartadplatform.application.converter.DtoConverter;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(DtoConverter.getCategoryToDtoConverter());
        modelMapper.addConverter(DtoConverter.getItemToDtoConverter());
        return modelMapper;
    }

}
