package com.sparta.barointern.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.barointern.dto.SignupResponse;
import com.sparta.barointern.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{userId}/roles")
	public ResponseEntity<SignupResponse> grantAdmin(@PathVariable Long userId) {

		SignupResponse response = adminService.grantAdminRole(userId);
		return ResponseEntity.ok(response);
	}
}
