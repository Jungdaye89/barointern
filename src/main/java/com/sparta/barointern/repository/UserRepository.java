package com.sparta.barointern.repository;

import java.util.Optional;

import com.sparta.barointern.model.User;

public interface UserRepository {

	User save(User user);
	Optional<User> findByUsername(String username);
	Optional<User> findById(Long id);
}
