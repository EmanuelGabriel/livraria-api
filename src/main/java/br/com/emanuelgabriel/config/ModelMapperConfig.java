package br.com.emanuelgabriel.config;

import org.modelmapper.ModelMapper;

// @Configuration
public class ModelMapperConfig {

    // @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
