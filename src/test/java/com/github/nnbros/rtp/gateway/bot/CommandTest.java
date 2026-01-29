package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import com.github.nnbros.rtp.gateway.exception.UpdateRuntimeException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.stream.Stream;

import static com.github.nnbros.rtp.gateway.BotTestUtils.createTestCommandMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandTest {

	@ParameterizedTest
	@MethodSource("commandProvider")
	public void parseCommand(String input, Command expected) {
		Message message = createTestCommandMessage(input);

		Command result = Command.parseCommand(message);

		assertEquals(expected, result);
	}

	@ParameterizedTest
	@MethodSource("commandWithExceptionProvider")
	public void parseCommandWithException(String input, Command expected) {
		Message message = createTestCommandMessage(input);

		UpdateRuntimeException exception = assertThrows(UpdateRuntimeException.class, () -> Command.parseCommand(message));
		assertEquals(expected.getCommandResponse(), exception.getMessage());
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
				Arguments.of("/start", Command.START)
		);
	}

	static Stream<Arguments> commandWithExceptionProvider() {
		return Stream.of(
				Arguments.of("/HELP", Command.HELP),
				Arguments.of("/help", Command.HELP),
				Arguments.of("/NON_EXISTENT_COMMAND", Command.UNKNOWN)
		);
	}
}


