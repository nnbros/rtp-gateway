package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class UnregisteredUsersCleanerTest {
	@Mock
	private UserService userService;
	@Mock
	private StorytellerServiceClient storytellerServiceClient;
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
		Mockito.verify(storytellerServiceClient, Mockito.times(1)).unregisterUsers(ArgumentMatchers.anySet());
	}
}
