package com.sparta.barointern.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	private static final String SECURITY_SCHEME_NAME = "bearerAuth";

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()

			.info(new Info()
				.title("Barointern API 문서")
				.version("v1.0.0")
				.description("회원가입, 로그인, 관리자 권한 부여 API"))
			.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
			.components(new io.swagger.v3.oas.models.Components()
				.addSecuritySchemes(SECURITY_SCHEME_NAME, createBearerScheme()));
	}

	private SecurityScheme createBearerScheme() {
		return new SecurityScheme()
			.name("Authorization")
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT");
	}
}
