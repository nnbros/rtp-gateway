package com.github.nnbros.rtp.gateway.bot.action.provider;

import com.github.nnbros.rtp.gateway.bot.action.Action;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Stream;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.TEST_USER_ID;
import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.createTestCallbackQueryUpdate;
import static com.github.nnbros.rtp.gateway.bot.action.provider.CallbackQueryActionProvider.STORYTELLER_CREATE_CHAR_CANCEL;
import static com.github.nnbros.rtp.gateway.bot.action.provider.CallbackQueryActionProvider.STORYTELLER_CREATE_CHAR_START;
import static com.github.nnbros.rtp.gateway.bot.action.provider.MessageActionProvider.STORYTELLER_CREATE_CHAR_GENDER;
import static com.github.nnbros.rtp.gateway.bot.action.provider.MessageActionProvider.STORYTELLER_CREATE_CHAR_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CallbackQueryActionProviderTest {
	@InjectMocks
	private CallbackQueryActionProvider provider;

	@ParameterizedTest
	@MethodSource("lastActions")
	void checkAction(String actionId, Action expected) {
		Update update = createTestCallbackQueryUpdate(actionId);
		Action actual = provider.retrieve(update);

		assertEquals(expected, actual);
	}

	private static Stream<Arguments> lastActions() {
		return Stream.of(
				Arguments.of(STORYTELLER_CREATE_CHAR_START, new Action(STORYTELLER_CREATE_CHAR_START,
						null, TEST_USER_ID, createTestCallbackQueryUpdate(STORYTELLER_CREATE_CHAR_START))),
				Arguments.of(STORYTELLER_CREATE_CHAR_CANCEL, new Action(STORYTELLER_CREATE_CHAR_START,
						null, TEST_USER_ID, createTestCallbackQueryUpdate(STORYTELLER_CREATE_CHAR_CANCEL))),
				Arguments.of(STORYTELLER_CREATE_CHAR_GENDER + ":female", new Action(STORYTELLER_CREATE_CHAR_GENDER,
						"female", TEST_USER_ID, createTestCallbackQueryUpdate(STORYTELLER_CREATE_CHAR_GENDER + ":female"))),
				Arguments.of(STORYTELLER_CREATE_CHAR_NAME + ":", new Action(STORYTELLER_CREATE_CHAR_NAME,
						null, TEST_USER_ID, createTestCallbackQueryUpdate(STORYTELLER_CREATE_CHAR_NAME)))
		);
	}
}
