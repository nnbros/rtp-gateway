package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.bot.statemachine.StateMachineService;
import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnregisteredUsersCleaner {
	private final UserService userService;
	private final StateMachineService stateMachineService;
	private final StorytellerServiceClient storytellerServiceClient;

	@Scheduled(initialDelayString = "${gateway.user.unregistered.ttl}", fixedRateString = "${gateway.user.unregistered.ttl}")
	public void removeUnregisteredUsers() {
		Set<Long> usersToDelete = userService.unregisteredUsersToDelete();
		log.debug("Starting scheduled remove unregistered users process...");
		if (!usersToDelete.isEmpty()) {
			for (Long userId : usersToDelete) {
				userService.remove(userId);
				stateMachineService.removeStateMachineForUser(userId.toString());
			}
			log.info("Removed unregistered users with ids={} from cache", usersToDelete.stream()
					.map(Object::toString)
					.collect(Collectors.joining(",")));
			storytellerServiceClient.unregisterUsers(usersToDelete);
		}
		log.debug("Stopped scheduled remove unregistered users...");
	}
}
