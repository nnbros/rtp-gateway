package com.github.nnbros.rtp.gateway.bot.update.provider;

import com.github.nnbros.rtp.gateway.bot.Command;
import com.github.nnbros.rtp.gateway.bot.update.Action;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Stream;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CommandActionProviderTest {
	@InjectMocks
	private CommandActionProvider provider;

	@ParameterizedTest
	@MethodSource("updates")
	public void checkAction(Update update, Action expected) {
		Action actual = provider.retrieve(update);

		assertEquals(expected, actual);
	}

	@Test
	void checkActionException() {
		GatewayRuntimeException actual = assertThrows(GatewayRuntimeException.class, () -> provider.retrieve(createTestMessageUpdate()));
		assertEquals("Unable to process the update 456. It's not a command", actual.getMessage());
	}

	@Test
	void checkActionNPException() {
		NullPointerException actual = assertThrows(NullPointerException.class, () -> provider.retrieve(createTestEmptyUpdate()));
		assertEquals("Message cannot be empty", actual.getMessage());
	}

	private static Stream<Arguments> updates() {
		return Stream.of(
				Arguments.of(createTestCommandUpdate(Command.HELP), new Action(Command.HELP.getAction(),
						Command.HELP.getCommandResponse(), createTestCommandUpdate(Command.HELP), TEST_USER_ID)),
				Arguments.of(createTestEditedCommandUpdate(Command.HELP), new Action(Command.HELP.getAction(),
						Command.HELP.getCommandResponse(), createTestEditedCommandUpdate(Command.HELP), TEST_USER_ID)),
				Arguments.of(createTestCommandUpdate(Command.START), new Action(Command.START.getAction(),
						Command.START.getCommandResponse(), createTestCommandUpdate(Command.START), TEST_USER_ID)),
				Arguments.of(createTestCommandUpdate(Command.UNKNOWN), new Action(Command.UNKNOWN.getAction(),
						Command.UNKNOWN.getCommandResponse(), createTestCommandUpdate(Command.UNKNOWN), TEST_USER_ID))
		);
	}
}
