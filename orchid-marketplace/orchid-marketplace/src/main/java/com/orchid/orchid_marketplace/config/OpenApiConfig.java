package com.orchid.orchid_marketplace.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orchidOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orchid Marketplace API")
                        .version("0.0.1")
                        .description("API documentation for the Orchid Marketplace backend")
                        .contact(new Contact().name("Orchid Marketplace").email("dev@orchid.example"))
                );
    }
}
