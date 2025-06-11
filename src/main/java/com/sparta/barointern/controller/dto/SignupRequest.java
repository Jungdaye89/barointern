package com.sparta.barointern.controller.dto;

public record SignupRequest(

	String username,
	String password,
	String nickname
) {
}
