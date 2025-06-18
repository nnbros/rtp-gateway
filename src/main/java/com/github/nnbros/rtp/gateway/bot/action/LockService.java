package com.github.nnbros.rtp.gateway.bot.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * User is locked means that some action is in progress.
 * We shouldn't process other actions before the first action is completed
 */

@Slf4j
@Service
public class LockService {
	private final Map<Long, Instant> lockedUsers;
	@Value("${gateway.user.lock.ttl}")
	private Duration ttl;

	public LockService() {
		this.lockedUsers = new HashMap<>();
	}

	public boolean isLocked(Long userId) {
		return lockedUsers.containsKey(userId);
	}

	public void createLock(Long userId) {
		lockedUsers.put(userId, Instant.now());
		log.debug("Created lock for user = {}", userId);
	}

	public void releaseLock(Long userId) {
		lockedUsers.remove(userId);
		log.debug("Released lock for user = {}", userId);
	}

	@Scheduled(initialDelayString = "${gateway.user.lock.ttl}", fixedRateString = "${gateway.user.lock.ttl}")
	public void releaseLocks() {
		lockedUsers.entrySet()
				.removeIf(entry -> Duration.between(entry.getValue(), Instant.now()).compareTo(ttl) > 0);
		log.debug("Scheduled job for user release lock has been executed successfully");
	}
}
