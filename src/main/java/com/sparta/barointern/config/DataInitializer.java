package com.sparta.barointern.config;

import com.sparta.barointern.exception.CustomException;
import com.sparta.barointern.exception.ExceptionCode;
import com.sparta.barointern.model.User;
import com.sparta.barointern.model.Role;
import com.sparta.barointern.repository.UserRepository;
import com.sparta.barointern.service.UserService;
import com.sparta.barointern.dto.SignupRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
	private final UserService userService;
	private final UserRepository userRepository;

	public DataInitializer(UserService userService,
		UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}

	@Override
	public void run(String... args) {
		String adminUsername = "admin";
		String adminPassword = "admin1234";
		String adminNickname = "Administrator";

		try {
			userService.signup(new SignupRequest(adminUsername, adminPassword, adminNickname));
		} catch (CustomException e) {
			if (e.getExceptionCode() != ExceptionCode.USER_ALREADY_EXISTS) {
				throw e;
			}
		}

		User admin = userRepository.findByUsername(adminUsername)
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

		if (admin.getRole() != Role.ADMIN) {
			User updated = admin.toBuilder()
				.role(Role.ADMIN)
				.build();
			userRepository.save(updated);
		}

		System.out.println("관리자 권한 부여");
	}
}
