package com.blackstone.customer.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customerServiceOpenAPI() {
        return new OpenAPI().components(new Components())
                .info(new Info()
                        .title("Customer Service API")
                        .description("API for managing customers")
                        .version("1.0.0")
                        .contact(new Contact().name("Dev Team").email("ahmed.m.labeb@gmail.com")))
                ;
    }
}
