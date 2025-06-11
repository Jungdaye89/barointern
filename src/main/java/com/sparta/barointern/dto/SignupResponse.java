package com.sparta.barointern.dto;

import com.sparta.barointern.model.Role;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SignupResponse", description = "회원가입 응답 DTO")
public record SignupResponse(

	@Schema(description = "가입된 로그인 아이디", example = "user")
	String username,
	@Schema(description = "가입된 사용자 별명", example = "민트")
	String nickname,
	@ArraySchema(
		schema = @Schema(description = "사용자 권한 목록", implementation = Role.class),
		arraySchema = @Schema(description = "USER, ADMIN 중 하나의 권한")
	)
	Set<Role> roles
) {}
