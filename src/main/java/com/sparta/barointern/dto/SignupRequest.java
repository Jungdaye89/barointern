package com.sparta.barointern.dto;

public record SignupRequest(

	String username,
	String password,
	String nickname
) {
}
