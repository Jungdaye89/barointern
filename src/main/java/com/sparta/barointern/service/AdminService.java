package com.sparta.barointern.service;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sparta.barointern.dto.SignupResponse;
import com.sparta.barointern.exception.CustomException;
import com.sparta.barointern.exception.ExceptionCode;
import com.sparta.barointern.model.Role;
import com.sparta.barointern.model.User;
import com.sparta.barointern.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final UserRepository userRepository;

	public SignupResponse grantAdminRole(Long userId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

		User updated = user.toBuilder()
				.role(Role.ADMIN)
				.build();

		updated = userRepository.save(updated);

		return new SignupResponse(
			updated.getUsername(),
			updated.getNickname(),
			Set.of(updated.getRole())
		);
	}
}
