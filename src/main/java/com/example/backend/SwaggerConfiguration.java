package com.example.backend;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme basicSecurityScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic");
        Components components = new Components().addSecuritySchemes("basicAuth", basicSecurityScheme);

        return new OpenAPI()
                .components(components);
    }
}
