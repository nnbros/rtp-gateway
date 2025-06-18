package com.github.nnbros.rtp.gateway.bot.controller;

import com.github.nnbros.rtp.gateway.bot.action.LockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.TEST_USER_ID;

@ExtendWith(MockitoExtension.class)
class UserManagementControllerTest {
	@Mock
	private LockService lockService;
	@InjectMocks
	private UserManagementController userManagementController;

	@Test
	void checkReleaseLock() {
		userManagementController.releaseLock(TEST_USER_ID);
		Mockito.verify(lockService, Mockito.times(1)).releaseLock(ArgumentMatchers.anyLong());
	}
}
