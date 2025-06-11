package com.sparta.barointern.controller;

import com.sparta.barointern.dto.LoginRequest;
import com.sparta.barointern.dto.LoginResponse;
import com.sparta.barointern.dto.SignupRequest;
import com.sparta.barointern.exception.CustomException;
import com.sparta.barointern.exception.ExceptionCode;
import com.sparta.barointern.model.Role;
import com.sparta.barointern.model.User;
import com.sparta.barointern.repository.UserRepository;
import com.sparta.barointern.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	private String adminToken;

	@BeforeEach
	void setUp() {

		SignupRequest adminSignup =
			new SignupRequest(
				"adminUser",
				"adminPass",
				"Administrator");
		userService.signup(adminSignup);


		User admin = userRepository.findByUsername("adminUser")
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

		userRepository.save(
			admin.toBuilder()
				.role(Role.ADMIN)
				.build()
		);

		LoginResponse loginResp = userService.login(
			new LoginRequest("adminUser", "adminPass")
		);
		adminToken = loginResp.token();


	}

	@Test
	@DisplayName("권한 없는 사용자 요청 시 ACCESS_DENIED: USER 토큰으로 PATCH 요청 시 403 반환")
	void grantAdmin_accessDenied() throws Exception {

		userService.signup(new SignupRequest
			("normalUser", "userPass", "Normal"));
		LoginResponse loginResp = userService.login(
			new LoginRequest("normalUser", "userPass")
		);

		String userToken = loginResp.token();

		User normal = userRepository.findByUsername("normalUser").get();

		mockMvc.perform(
				patch("/admin/users/" + normal.getId() + "/roles")
					.header("Authorization", userToken)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.error.code").value(ExceptionCode.ACCESS_DENIED.name()));
	}
}
