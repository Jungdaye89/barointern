package com.sparta.barointern.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;

import com.sparta.barointern.dto.LoginRequest;
import com.sparta.barointern.dto.LoginResponse;
import com.sparta.barointern.dto.SignupRequest;
import com.sparta.barointern.dto.SignupResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "회원가입, 로그인 API")
public interface UserControllerDocs {

	@Operation(summary = "회원가입", description = "새로운 사용자 회원가입")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "회원가입 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = SignupResponse.class),
				examples = @ExampleObject(
					name = "회원가입 성공",
					value = "{\n  \"username\": \"JIN HO\",\n  \"nickname\": \"Mentos\",\n  \"roles\": [{ \"role\": \"USER\" }]\n}"
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "이미 가입된 사용자",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "회원가입 실패",
					value = "{\n  \"error\": {\n    \"code\": \"USER_ALREADY_EXISTS\",\n    \"message\": \"이미 가입된 사용자입니다.\"\n  }\n}"
				)
			)
		)
	})
	@PostMapping("/signup")
	ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request);

	@Operation(summary = "로그인", description = "아이디/비밀번호로 로그인 후 JWT 토큰 발급")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "로그인 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = LoginResponse.class),
				examples = @ExampleObject(
					name = "로그인 성공",
					value = "{\n  \"token\": \"eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL\"\n}"
				)
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "잘못된 자격 증명",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "로그인 실패",
					value = "{\n  \"error\": {\n    \"code\": \"INVALID_CREDENTIALS\",\n    \"message\": \"아이디 또는 비밀번호가 올바르지 않습니다.\"\n  }\n}"
				)
			)
		)
	})
	@PostMapping("/login")
	ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request);
}
