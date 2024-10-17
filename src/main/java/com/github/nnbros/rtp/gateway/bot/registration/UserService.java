package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.model.User;
import com.github.nnbros.rtp.gateway.repository.UserRepository;
import com.github.nnbros.rtp.gateway.util.ActionError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository repository;
	private final Map<Long, UnregisteredUserContext> unregisteredUsers = new HashMap<>();
	@Value("${gateway.user.unregistered.ttl}")
	private Duration ttl;

	public boolean exists(Long userId) {
		return repository.existsById(userId);
	}

	public Optional<String> getLastAction(Long userId) {
		if (unregisteredUsers.containsKey(userId)) {
			return Optional.ofNullable(unregisteredUsers.get(userId).getLastAction());
		} else {
			return repository.findById(userId)
					.map(User::getLastAction);
		}
	}

	public void updateLastAction(Long userId, String lastAction) {
		if (exists(userId)) {
			repository.updateLastAction(userId, lastAction);
		} else {
			unregisteredUsers.merge(userId,
					UnregisteredUserContext.builder()
							.createTime(Instant.now())
							.lastAction(lastAction)
							.build(), (contextOld, contextNew) -> {
						contextNew.setCreateTime(contextOld.getCreateTime());
						return contextNew;
					});
		}
	}

	public void remove(Long userId) {
		unregisteredUsers.remove(userId);
	}

	public Set<Long> usersToDelete() {
		return unregisteredUsers.entrySet().stream()
				.filter(entry -> Duration.between(entry.getValue().getCreateTime(), Instant.now()).compareTo(ttl) > 0)
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
	}

	public void create(Long userId, String username) {
		User user = new User();
		user.setId(userId);
		user.setUsername(username);
		String lastAction = getLastAction(user.getId()).orElse(ActionError.ERROR_UNKNOWN.name());
		user.setLastAction(lastAction);
		repository.save(user);
		// after save user is considered to be registered, so remove the user from the unregistered users map
		unregisteredUsers.remove(userId);
	}
}
