package com.sparta.barointern.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.barointern.dto.SignupRequest;
import com.sparta.barointern.exception.CustomException;
import com.sparta.barointern.exception.ExceptionCode;
import com.sparta.barointern.model.Role;
import com.sparta.barointern.model.User;
import com.sparta.barointern.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void initAdmin() {

		SignupRequest adminReq =
			new SignupRequest(
				"admin",
				"1234",
				"admin");
		userService.signup(adminReq);

		User admin = userRepository.findByUsername("admin")
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

		userRepository.save(
			admin.toBuilder()
				.role(Role.ADMIN)
				.build()
		);
	}

	@AfterEach
	void clearAuthentication() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("관리자 권한 부여 성공: ADMIN 권한 사용자 요청 시 정상 처리")
	void grantAdmin_success() {

		Authentication auth = new UsernamePasswordAuthenticationToken(
			"adminUser", null,
			List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
		);
		SecurityContextHolder.getContext().setAuthentication(auth);


		SignupRequest request =
			new SignupRequest(
				"targetUser",
				"pass",
				"Target");
		userService.signup(request);

		User target = userRepository.findByUsername("targetUser")
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

		var response = adminService.grantAdminRole(target.getId());
		assertEquals("targetUser", response.username());
		assertTrue(
			response.roles().stream()
				.anyMatch(role -> "ADMIN".equals(role.getRole()))
		);
	}

	@Test
	@DisplayName("존재하지 않는 사용자에 권한 부여 시 USER_NOT_FOUND 예외 발생")
	void grantAdmin_userNotFound() {

		Authentication auth = new UsernamePasswordAuthenticationToken(
			"adminUser", null,
			List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
		);
		SecurityContextHolder.getContext().setAuthentication(auth);

		CustomException customException = assertThrows(
			CustomException.class,
			() -> adminService.grantAdminRole(7L)
		);
		assertEquals(ExceptionCode.USER_NOT_FOUND, customException.getExceptionCode());
	}
}
