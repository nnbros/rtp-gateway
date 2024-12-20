package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.model.User;
import com.github.nnbros.rtp.gateway.repository.UserRepository;
import com.github.nnbros.rtp.gateway.util.ActionError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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
		return Optional.ofNullable(unregisteredUsers.get(userId))
				.map(UnregisteredUserContext::getLastAction)
				.or(() -> repository.findById(userId)
						.map(User::getLastAction));
	}

	@Transactional
	public void updateLastAction(Long userId, String lastAction) {
		log.debug("Last action updated to {} for user id = {}", userId, lastAction);
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

	public Set<Long> unregisteredUsersToDelete() {
		return unregisteredUsers.entrySet().stream()
				.filter(entry -> Duration.between(entry.getValue().getCreateTime(), Instant.now()).compareTo(ttl) > 0)
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
	}

	public void create(Long userId, String username) {
		log.debug("A new user id={}, name={} was created", userId, username);
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
