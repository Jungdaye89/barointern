package com.sparta.barointern.model;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private Long id;
	private String username;
	private String password;
	private String nickname;
	private Role role;

	public static User create(
		String username,
		String password,
		String nickname,
		PasswordEncoder passwordEncoder
	) {
		return User.builder()
			.id(null)
			.username(username)
			.password(passwordEncoder.encode(password))
			.nickname(nickname)
			.role(Role.USER)
			.build();
	}
}
