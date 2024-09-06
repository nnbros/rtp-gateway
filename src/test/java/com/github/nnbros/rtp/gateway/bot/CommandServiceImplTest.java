package com.github.nnbros.rtp.gateway.bot;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.stream.Stream;

class CommandServiceImplTest {
	private final MessageBuilder messageBuilder = new MessageBuilderImpl();
	private final CommandServiceImpl commandService = new CommandServiceImpl(messageBuilder);

	@ParameterizedTest
	@MethodSource("provideCommands")
	void processCommand(Command command, String expectedResponse) {
		Message message = createTestCommandMessage("/" + command.name().toLowerCase());
		Update update = createTestMessageUpdate(message);

		BotApiMethod<?> response = commandService.processUpdate(update);

		assertInstanceOf(SendMessage.class, response);
		assertEquals(expectedResponse, ((SendMessage) response).getText());
	}

	@Test
	void processUpdateIsNotCommand() {
		Message message = createTestMessage();
		Update update = createTestMessageUpdate(message);

		assertThrows(GatewayRuntimeException.class, () -> commandService.processUpdate(update));
	}

	@Test
	void processUpdateWhenMessageIsNull() {
		Update update = createTestEmptyUpdate();

		assertThrows(NullPointerException.class, () -> commandService.processUpdate(update));
	}

	private static Stream<Arguments> provideCommands() {
		return Stream.of(
				Arguments.of(Command.START, Command.START.getCommandResponse()),
				Arguments.of(Command.HELP, Command.HELP.getCommandResponse()),
				Arguments.of(Command.UNKNOWN, Command.UNKNOWN.getCommandResponse())
		);
	}
}

