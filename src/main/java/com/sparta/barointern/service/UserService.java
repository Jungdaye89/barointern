package com.sparta.barointern.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.barointern.controller.dto.SignupRequest;
import com.sparta.barointern.controller.dto.SignupResponse;
import com.sparta.barointern.exception.CustomException;
import com.sparta.barointern.exception.ExceptionCode;
import com.sparta.barointern.model.User;
import com.sparta.barointern.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SignupResponse signup(SignupRequest signupRequest) {

		userRepository.findByUsername(signupRequest.username())
			.ifPresent(user -> {
				throw new CustomException(ExceptionCode.USER_ALREADY_EXISTS);
			});

		User user = User.create(
			signupRequest.username(),
			signupRequest.password(),
			signupRequest.nickname(),
			passwordEncoder
		);

		User saved = userRepository.save(user);

		return new SignupResponse(
			saved.getUsername(),
			saved.getNickname(),
			Set.of(saved.getRole())
		);
	}
}
