package com.sparta.barointern.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginRequest", description = "로그인 요청 DTO")
public record LoginRequest(
	@Schema(description = "로그인 아이디", example = "user")
	String username,
	@Schema(description = "계정 비밀번호", example = "P@ssw0rd12!")
	String password
) {}
