package com.sparta.barointern.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.barointern.controller.dto.LoginRequest;
import com.sparta.barointern.controller.dto.LoginResponse;
import com.sparta.barointern.controller.dto.SignupRequest;
import com.sparta.barointern.controller.dto.SignupResponse;
import com.sparta.barointern.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	public final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {

		SignupResponse response = userService.signup(signupRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

		LoginResponse response = userService.login(loginRequest);
		return ResponseEntity.ok(response);
	}
}
