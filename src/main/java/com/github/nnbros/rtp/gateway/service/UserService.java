package com.github.nnbros.rtp.gateway.service;

import com.github.nnbros.rtp.gateway.model.User;
import com.github.nnbros.rtp.gateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository repository;
	private final Map<Long, String> unregisteredUserLastActions;

	@Cacheable
	public boolean exists(Long userId) {
		return repository.existsById(userId);
	}

	public String getLastAction(Long userId) {
		return unregisteredUserLastActions.get(userId);
	}

	public void updateLastAction(Long userId, String lastAction) {
		if (exists(userId)) {
			repository.updateLastAction(userId, lastAction);
		} else {
			unregisteredUserLastActions.put(userId, lastAction);
		}
	}

	// uncache
	public void create(Long userId, String username) {
		User user = new User();
		user.setId(userId);
		user.setUsername(username);
		String lastAction = unregisteredUserLastActions.get(user.getId());
		user.setLastAction(lastAction);
		repository.save(user);
	}
}
