package com.sparta.barointern.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse", description = "로그인 응답 DTO")
public record LoginResponse(
	@Schema(
		description = "발급된 JWT 토큰",
		example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsI..."
	)
	String token
) {
}
