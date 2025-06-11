package com.sparta.barointern.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SignupRequest", description = "회원가입 요청 DTO")
public record SignupRequest(
	@Schema(description = "회원 가입할 로그인 아이디", example = "user")
	String username,
	@Schema(description = "계정 비밀번호", example = "P@ssw0rd12!")
	String password,
	@Schema(description = "사용자 별명", example = "민트")
	String nickname
) {
}
