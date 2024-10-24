package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.model.User;
import com.github.nnbros.rtp.gateway.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.TEST_USER_ID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	public static final String ACTION = "action";
	public static final String USERNAME = "TestName";
	@Mock
	private UserRepository repository;
	@InjectMocks
	private UserService userService;

	@Test
	void checkUpdateLastActionForRegisteredUser() {
		Mockito.when(repository.existsById(TEST_USER_ID)).thenReturn(true);
		User user = new User();
		user.setLastAction(ACTION);
		Mockito.when(repository.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
		userService.updateLastAction(TEST_USER_ID, ACTION);
		Assertions.assertEquals(userService.getLastAction(TEST_USER_ID), Optional.of(ACTION));
	}

	@Test
	void checkUpdateLastAction() {
		userService.updateLastAction(TEST_USER_ID, ACTION);
		Assertions.assertEquals(userService.getLastAction(TEST_USER_ID), Optional.of(ACTION));
	}

	@Test
	void checkLastActionUpdatedTwice() {
		userService.updateLastAction(TEST_USER_ID, "old action");
		userService.updateLastAction(TEST_USER_ID, ACTION);
		Assertions.assertEquals(userService.getLastAction(TEST_USER_ID), Optional.of(ACTION));
	}

	@Test
	void checkCreateUser() {
		userService.updateLastAction(TEST_USER_ID, ACTION);
		userService.create(TEST_USER_ID, USERNAME);
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
	}

	@Test
	void checkUsersToDelete() {
		userService.updateLastAction(TEST_USER_ID, ACTION);
		userService.updateLastAction(124L, ACTION);
		ReflectionTestUtils.setField(userService, "ttl", Duration.ofMillis(0));

		Set<Long> actual = userService.unregisteredUsersToDelete();
		Assertions.assertEquals(Set.of(TEST_USER_ID, 124L), actual);
	}
}
