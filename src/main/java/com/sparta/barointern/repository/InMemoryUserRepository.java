package com.sparta.barointern.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.sparta.barointern.model.User;

@Repository
public class InMemoryUserRepository implements UserRepository {

	private final Map<Long, User> users = new HashMap<>();
	private final AtomicLong userIdGenerator = new AtomicLong(0);

	@Override
	public User save(User user) {

		if (user.getId() == null) {
			long newId = userIdGenerator.incrementAndGet();

			user = user.toBuilder()
				.id(newId)
				.build();
		}

		users.put(user.getId(), user);

		return user;
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return users.values().stream()
			.filter(user -> user.getUsername().equals(username))
			.findFirst();
	}

	@Override
	public Optional<User> findById(Long id) {
		return Optional.ofNullable(users.get(id));
	}
}
