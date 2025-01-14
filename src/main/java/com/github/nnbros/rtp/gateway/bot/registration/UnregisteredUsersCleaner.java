package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnregisteredUsersCleaner {
	private final UserService userService;
	private final StateMachineService<States, Events> stateMachineService;
	private final StorytellerServiceClient storytellerServiceClient;
	private final JpaStateMachineRepository stateMachineRepository;

	@Scheduled(initialDelayString = "${gateway.user.unregistered.ttl}", fixedRateString = "${gateway.user.unregistered.ttl}")
	public void removeUnregisteredUsers() {
		Set<Long> usersToDelete = userService.unregisteredUsersToDelete();
		log.debug("Starting scheduled remove unregistered users process...");
		if (!usersToDelete.isEmpty()) {
			for (Long userId : usersToDelete) {
				userService.remove(userId);
				String machineId = userId.toString();
				stateMachineService.releaseStateMachine(machineId, true);
				stateMachineRepository.deleteById(machineId);
			}
			log.info("Removed unregistered users with ids={} from cache", usersToDelete.stream()
					.map(Object::toString)
					.collect(Collectors.joining(",")));
			storytellerServiceClient.unregisterUsers(usersToDelete);
		}
		log.debug("Stopped scheduled remove unregistered users...");
	}
}
