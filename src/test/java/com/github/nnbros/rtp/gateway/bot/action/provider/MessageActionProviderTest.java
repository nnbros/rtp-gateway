package com.github.nnbros.rtp.gateway.bot.action.provider;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.action.Action;
import com.github.nnbros.rtp.gateway.exception.UnknownMessageException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.stream.Stream;

import static com.github.nnbros.rtp.gateway.BotTestUtils.*;
import static com.github.nnbros.rtp.gateway.bot.action.provider.CallbackQueryActionProvider.STORYTELLER_CREATE_CHAR_START;
import static com.github.nnbros.rtp.gateway.bot.action.provider.MessageActionProvider.STORYTELLER_CREATE_CHAR_GENDER;
import static com.github.nnbros.rtp.gateway.bot.action.provider.MessageActionProvider.STORYTELLER_CREATE_CHAR_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MessageActionProviderTest {
	@Mock
	private UserService userService;
	@InjectMocks
	private MessageActionProvider provider;

	@Test
	void checkActionNPException() {
		NullPointerException actual = assertThrows(NullPointerException.class, () -> provider.retrieve(createTestEmptyUpdate()));
		assertEquals("Message cannot be empty", actual.getMessage());
	}

	@Test
	void checkUnknownAction() {
		Update update = createTestEditedMessageUpdate();
		assertThrows(UnknownMessageException.class, () -> provider.retrieve(update));
	}

	@ParameterizedTest
	@MethodSource("lastActions")
	void checkAction(String actionId, Action expected) {
		Update update = createTestMessageUpdate();
		Mockito.when(userService.getLastAction(TEST_USER_ID)).thenReturn(Optional.of(actionId));
		Action actual = provider.retrieve(update);

		assertEquals(expected, actual);
	}

	@ParameterizedTest
	@MethodSource("lastActionsException")
	void checkActionException(String actionId) {
		Update update = createTestMessageUpdate();
		Mockito.when(userService.getLastAction(TEST_USER_ID)).thenReturn(Optional.of(actionId));

		assertThrows(UnknownMessageException.class, () -> provider.retrieve(update));
	}

	private static Stream<Arguments> lastActions() {
		return Stream.of(
				Arguments.of(STORYTELLER_CREATE_CHAR_GENDER, new Action(STORYTELLER_CREATE_CHAR_NAME,
						null, TEST_USER_ID, createTestMessageUpdate())),
				Arguments.of(STORYTELLER_CREATE_CHAR_NAME, new Action(STORYTELLER_CREATE_CHAR_NAME,
						null, TEST_USER_ID, createTestMessageUpdate()))
		);
	}

	private static Stream<Arguments> lastActionsException() {
		return Stream.of(
				Arguments.of("blablabla"),
				Arguments.of(STORYTELLER_CREATE_CHAR_START)
		);
	}
}
