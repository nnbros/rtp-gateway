package com.github.nnbros.rtp.gateway.bot.update;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.update.processor.ActionProcessor;
import com.github.nnbros.rtp.gateway.bot.update.processor.UserUpdateActionProcessor;
import com.github.nnbros.rtp.gateway.configuration.Actions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.TEST_USER_ID;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActionRouterTest {
	@Mock
	private UserService userService;
	@Mock
	private UserUpdateActionProcessor userUpdateActionProcessor;
	private ActionRouter actionRouter;
	private static final String TEST_ACTION = "action";
	private static final String OTHER_TEST_ACTION = "other_action";

	@BeforeEach
	void init() {
		Actions actions = new Actions();
		actions.setAllowedForUnregisteredUsers(Set.of(TEST_ACTION));
		Map<String, List<ActionProcessor>> actionProcessors = Map.of(
				TEST_ACTION, List.of(userUpdateActionProcessor),
				OTHER_TEST_ACTION, List.of(userUpdateActionProcessor));
		actionRouter = new ActionRouter(actionProcessors);
	}

	@Test
	void checkSuccessRoute() {
		actionRouter.route(new Action(TEST_ACTION, null, TEST_USER_ID, mock(Update.class)));
		verify(userUpdateActionProcessor, times(1)).process(ArgumentMatchers.any(Action.class));
	}
}
