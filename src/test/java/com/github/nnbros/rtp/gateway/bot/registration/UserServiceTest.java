package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.TEST_USER_ID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	public static final String ACTION = "action";
	@Mock
	private UserRepository repository;
	@InjectMocks
	private UserService userService;

	@Test
	void checkUpdateLastActionForRegisteredUser() {
		Mockito.when(repository.existsById(TEST_USER_ID)).thenReturn(true);
		userService.updateLastAction(TEST_USER_ID, ACTION);
		Assertions.assertEquals(userService.getLastAction(TEST_USER_ID), ACTION);
	}

	@Test
	void checkUpdateLastAction() {
		userService.updateLastAction(TEST_USER_ID, ACTION);
		Assertions.assertEquals(userService.getLastAction(TEST_USER_ID), ACTION);
	}

	@Test
	void checkLastActionUpdatedTwice() {
		userService.updateLastAction(TEST_USER_ID, "old action");
		userService.updateLastAction(TEST_USER_ID, ACTION);
		Assertions.assertEquals(userService.getLastAction(TEST_USER_ID), ACTION);
	}
}
