package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import com.github.nnbros.rtp.gateway.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.stream.Stream;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandServiceImplTest {
	private final MessageBuilder messageBuilder = new MessageBuilderImpl();
	private final RegistrationService registrationService = mock(RegistrationService.class);
	private final CommandService commandService = new CommandService(registrationService, messageBuilder);

	@ParameterizedTest
	@MethodSource("provideCommands")
	void processCommand(Command command, String expectedResponse) {
		Message message = createTestCommandMessage("/" + command.name().toLowerCase());
		Update update = createTestMessageUpdate(message);
		Mockito.when(registrationService.startRegistration(TEST_USER_ID, update))
				.thenReturn(SendMessage.builder()
						.chatId(TEST_USER_ID)
						.text(expectedResponse)
						.build());

		BotApiMethod<?> response = commandService.process(TEST_USER_ID, update);

		assertInstanceOf(SendMessage.class, response);
		assertEquals(expectedResponse, ((SendMessage) response).getText());
	}

	@ParameterizedTest
	@MethodSource("provideCommands")
	void processEditedCommand(Command command, String expectedResponse) {
		Message message = createTestCommandMessage("/" + command.name().toLowerCase());
		Update update = createTestEditedMessageUpdate(message);
		Mockito.when(registrationService.startRegistration(TEST_USER_ID, update))
				.thenReturn(SendMessage.builder()
						.chatId(TEST_USER_ID)
						.text(expectedResponse)
						.build());
		BotApiMethod<?> response = commandService.process(TEST_USER_ID, update);

		assertInstanceOf(SendMessage.class, response);
		assertEquals(expectedResponse, ((SendMessage) response).getText());
	}

	@Test
	void processUpdateIsNotCommand() {
		Message message = createTestMessage();
		Update update = createTestMessageUpdate(message);

		assertThrows(GatewayRuntimeException.class, () -> commandService.process(TEST_USER_ID, update));
	}

	@Test
	void processUpdateWhenMessageIsNull() {
		Update update = createTestEmptyUpdate();

		assertThrows(NullPointerException.class, () -> commandService.process(TEST_USER_ID, update));
	}

	private static Stream<Arguments> provideCommands() {
		return Stream.of(
				Arguments.of(Command.START, Command.START.getCommandResponse()),
				Arguments.of(Command.HELP, Command.HELP.getCommandResponse()),
				Arguments.of(Command.UNKNOWN, Command.UNKNOWN.getCommandResponse())
		);
	}
}

