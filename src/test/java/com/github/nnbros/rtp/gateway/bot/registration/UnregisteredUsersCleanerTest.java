package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.service.StateMachineService;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class UnregisteredUsersCleanerTest {
	@Mock
	private UserService userService;
	@Mock
	private StorytellerServiceClient storytellerServiceClient;
	@Mock
	private StateMachineService<States, Events> stateMachineService;
	@Mock
	private JpaStateMachineRepository stateMachineRepository;
	@InjectMocks
	private UnregisteredUsersCleaner unregisteredUsersCleaner;

	@Test
	void checkUsersNothingToDelete() {
		unregisteredUsersCleaner.removeUnregisteredUsers();
		Mockito.verify(storytellerServiceClient, Mockito.never()).unregisterUsers(ArgumentMatchers.anySet());
	}

	@Test
	void checkUsersSomethingToDelete() {
		Mockito.when(userService.unregisteredUsersToDelete()).thenReturn(Set.of(1L, 2L, 3L));

		unregisteredUsersCleaner.removeUnregisteredUsers();
		Mockito.verify(userService, Mockito.times(3)).remove(ArgumentMatchers.anyLong());
		Mockito.verify(stateMachineService, Mockito.times(3)).releaseStateMachine(ArgumentMatchers.anyString(), eq(true));
		Mockito.verify(stateMachineRepository, Mockito.times(3)).deleteById(ArgumentMatchers.anyString());
		Mockito.verify(storytellerServiceClient, Mockito.times(1)).unregisterUsers(ArgumentMatchers.anySet());
	}
}
