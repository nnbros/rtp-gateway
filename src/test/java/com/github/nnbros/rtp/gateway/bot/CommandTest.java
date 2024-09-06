package com.github.nnbros.rtp.gateway.bot;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.createTestCommandMessage;
import static org.junit.jupiter.api.Assertions.*;

import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.stream.Stream;

public class CommandTest {

	@ParameterizedTest
	@MethodSource("commandProvider")
	public void parseCommand(String input, Command expected) {
		Message message = createTestCommandMessage(input);

		Command result = Command.parseCommand(message);

		assertEquals(expected, result);
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void parseCommandWithEmptyMessageText(String input) {
		Message message = createTestCommandMessage(input);

		assertThrows(GatewayRuntimeException.class, () -> Command.parseCommand(message));
	}

	static Stream<Arguments> commandProvider() {
		return Stream.of(
				Arguments.of("/START", Command.START),
				Arguments.of("/HELP", Command.HELP),
				Arguments.of("/start", Command.START),
				Arguments.of("/help", Command.HELP),
				Arguments.of("/NON_EXISTENT_COMMAND", Command.UNKNOWN)
		);
	}
}


