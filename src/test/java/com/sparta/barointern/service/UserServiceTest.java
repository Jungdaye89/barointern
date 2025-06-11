package com.sparta.barointern.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.barointern.dto.LoginRequest;
import com.sparta.barointern.dto.LoginResponse;
import com.sparta.barointern.dto.SignupRequest;
import com.sparta.barointern.dto.SignupResponse;
import com.sparta.barointern.exception.CustomException;
import com.sparta.barointern.exception.ExceptionCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

	@Autowired
	private UserService userService;

	@Test
	@DisplayName("회원가입 성공: 정상 정보 입력 시 USER 역할 포함한 응답 반환")
	void signup_success() {

		SignupRequest request =
			new SignupRequest(
				"test",
				"1234",
				"tester"
			);

		SignupResponse response =
			userService.signup(request);

		assertEquals("test", response.username());
		assertEquals("tester", response.nickname());
		assertTrue(
			response.roles().stream()
				.anyMatch(role -> "USER".equals(role.getRole()))
		);
	}

	@Test
	@DisplayName("회원가입 실패: 이미 존재하는 사용자 정보 입력 시 USER_ALREADY_EXISTS 예외 발생")
	void signup_alReadyExist() {

		SignupRequest request =
			new SignupRequest(
				"existUser",
				"1234",
				"existUser");
		userService.signup(request);

		CustomException customException =
			assertThrows(
				CustomException.class,
				() -> userService.signup(request)
			);

		assertEquals(ExceptionCode.USER_ALREADY_EXISTS, customException.getExceptionCode());
	}

	@Test
	@DisplayName("로그인 성공: 올바른 자격 증명으로 토큰 반환")
	void login_success() {

		SignupRequest request =
			new SignupRequest(
				"test",
				"1234",
				"tester"
			);

		userService.signup(request);

		LoginRequest loginRequest =
			new LoginRequest("test", "1234");

		LoginResponse loginResponse =
			userService.login(loginRequest);

		assertNotNull(loginResponse.token());
		assertFalse(loginResponse.token().isBlank());

		String[] parts =
			loginResponse.token().split("\\.");
		assertEquals(3, parts.length, "JWT는 헤더, 페이로드, 서명으로 구성되어야 한다");
	}

	@Test
	@DisplayName("로그인 실패: 잘못된 자격 증명으로 INVALID_CREDENTIALS 예외 발생")
	void login_invalidCredentials() {

		SignupRequest signup =
			new SignupRequest(
				"user1",
				"1234",
				"user1");
		userService.signup(signup);

		LoginRequest wrongPass =
			new LoginRequest("user1", "1357");
		CustomException customException1 = assertThrows(
			CustomException.class,
			() -> userService.login(wrongPass)
		);
		assertEquals(ExceptionCode.INVALID_CREDENTIALS, customException1.getExceptionCode());

		LoginRequest noUser =
			new LoginRequest("noUser", "1234");
		CustomException customException2 = assertThrows(
			CustomException.class,
			() -> userService.login(noUser)
		);
		assertEquals(ExceptionCode.INVALID_CREDENTIALS, customException2.getExceptionCode());
	}
}