package com.sparta.barointern.controller.dto;

import com.sparta.barointern.model.Role;
import java.util.Set;

public record SignupResponse(

	String username,
	String nickname,
	Set<Role> roles
) {}
