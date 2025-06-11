package com.sparta.barointern.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sparta.barointern.dto.SignupResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin", description = "관리자 권한 부여 API")
@RequestMapping("/admin/users")
public interface AdminControllerDocs {

	@Operation(summary = "관리자 권한 부여", description = "특정 사용자에게 ADMIN 권한을 부여")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "권한 부여 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = SignupResponse.class),
				examples = @ExampleObject(
					value = "{\n  \"username\": \"JIN HO\",\n  \"nickname\": \"Mentos\",\n  \"roles\": [{ \"role\": \"ADMIN\" }]\n}"
				)
			)
		),
		@ApiResponse(
			responseCode = "403",
			description = "접근 권한 없음",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					value = "{\n  \"error\": {\n    \"code\": \"ACCESS_DENIED\",\n    \"message\": \"관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.\"\n  }\n}"
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "사용자 없음",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					value = "{\n  \"error\": {\n    \"code\": \"USER_NOT_FOUND\",\n    \"message\": \"해당 사용자를 찾을 수 없습니다.\"\n  }\n}"
				)
			)
		)
	})
	@PatchMapping("/{userId}/roles")
	ResponseEntity<SignupResponse> grantAdmin(@PathVariable Long userId);
}
