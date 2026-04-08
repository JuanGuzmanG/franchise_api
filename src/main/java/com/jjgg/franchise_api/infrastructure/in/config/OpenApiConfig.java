package com.jjgg.franchise_api.infrastructure.in.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI franchiseApiOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Franchise API")
                .description("Reactive API for franchises, branches, and products")
                .version("v1")
                .contact(new Contact().name("Franchise API Team"))
        );
    }
}

