package com.github.nnbros.rtp.gateway.bot.update;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.TEST_USER_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class LockServiceTest {
	@InjectMocks
	private LockService lockService;

	@Test
	void checkUserLock() {
		lockService.createLock(TEST_USER_ID);
		assertTrue(lockService.isLocked(TEST_USER_ID));
	}

	@Test
	void checkUserUnlock() {
		lockService.createLock(TEST_USER_ID);
		lockService.releaseLock(TEST_USER_ID);
		assertFalse(lockService.isLocked(TEST_USER_ID));
	}

	@Test
	void checkMultipleUsersUnlock() {
		lockService.createLock(TEST_USER_ID);
		lockService.createLock(TEST_USER_ID + 1);

		ReflectionTestUtils.setField(lockService, "ttl", Duration.ofMillis(0));
		lockService.releaseLocks();

		assertFalse(lockService.isLocked(TEST_USER_ID));
		assertFalse(lockService.isLocked(TEST_USER_ID + 1));
	}
}
