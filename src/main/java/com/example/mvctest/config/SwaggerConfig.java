package com.example.mvctest.config;

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
                .title("MVCTest API Documentation")
                .version("1.0.0")
                .description("API documentation for the MVCTest project.");

        // JWT 인증 스키마 설정
        String jwtSchemeName = "jwtAuth";
        // SecurityRequirement: API 요청 시 필요한 보안 요구사항을 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        // Components: 재사용 가능한 OpenAPI 구성요소를 정의
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName) // 스키마 이름
                        .type(SecurityScheme.Type.HTTP) // 타입: HTTP
                        .scheme("bearer") // 스킴: bearer
                        .bearerFormat("JWT")); // 포맷: JWT

        // OpenAPI 객체를 생성하여 반환
        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement) // 모든 API에 보안 요구사항 추가
                .components(components); // 컴포넌트 추가
    }
}
