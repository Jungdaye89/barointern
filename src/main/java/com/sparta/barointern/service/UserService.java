package com.sparta.barointern.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.barointern.dto.LoginRequest;
import com.sparta.barointern.dto.LoginResponse;
import com.sparta.barointern.dto.SignupRequest;
import com.sparta.barointern.dto.SignupResponse;
import com.sparta.barointern.exception.CustomException;
import com.sparta.barointern.exception.ExceptionCode;
import com.sparta.barointern.model.User;
import com.sparta.barointern.repository.UserRepository;
import com.sparta.barointern.security.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

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

	public LoginResponse login(LoginRequest loginRequest) {

		User user = userRepository.findByUsername(loginRequest.username())
			.orElseThrow(() -> new CustomException(ExceptionCode.INVALID_CREDENTIALS));

		if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
			throw new CustomException(ExceptionCode.INVALID_CREDENTIALS);
		}

		String token =
			jwtProvider.generateToken(
				user.getUsername(),
				Set.of(user.getRole().name())
			);

		return new LoginResponse(token);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
	}
}
