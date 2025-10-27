package com.example.gamescord.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // API 정보 설정
        Info info = new Info()
                .title("gamescord API Documentation")
                .version("1.0.0")
                .description("API documentation for the gamescord project.");

        // OpenAPI 객체를 생성하여 반환
        return new OpenAPI()
                .info(info);
    }
}
