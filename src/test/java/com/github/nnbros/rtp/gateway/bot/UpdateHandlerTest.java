package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.bot.update.CommandService;
import com.github.nnbros.rtp.gateway.bot.update.LockService;
import com.github.nnbros.rtp.gateway.bot.update.UpdateService;
import com.github.nnbros.rtp.gateway.bot.update.UpdateType;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.*;
import static com.github.nnbros.rtp.gateway.bot.UpdateHandler.UNKNOWN_UPDATE_RESPONSE_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateHandlerTest {
	private final MessageBuilder messageBuilder = new MessageBuilderImpl();
	private final LockService lockService = new LockService();

	private final CommandService commandService = mock(CommandService.class);
	private final EnumMap<UpdateType, UpdateService> updateServiceMap = new EnumMap<>(
			Map.of(UpdateType.COMMAND, commandService, UpdateType.EDITED_COMMAND, commandService));
	private final UpdateHandler updateHandler = new UpdateHandler(updateServiceMap, lockService, messageBuilder);

	@ParameterizedTest
	@MethodSource("commandProvider")
	public void handleCommandUpdate(Update testUpdate) {
		BotApiMethod<?> testResponse = SendMessage.builder()
				.text("Test handleCommandUpdate")
				.chatId(testUpdate.hasMessage() ? testUpdate.getMessage().getChatId() : testUpdate.getEditedMessage().getChatId())
				.build();
		doReturn(testResponse).when(commandService).process(TEST_USER_ID, testUpdate);

		BotApiMethod<?> response = updateHandler.apply(testUpdate);

		verify(commandService, times(1)).process(TEST_USER_ID, testUpdate);
		assertEquals(testResponse, response);
	}

	@Test
	public void handleUnknownUpdate() {
		Update testUpdate = createTestMessageUpdate();

		BotApiMethod<?> response = updateHandler.apply(testUpdate);

		verifyNoInteractions(commandService);
		assertInstanceOf(SendMessage.class, response);
		assertEquals(UNKNOWN_UPDATE_RESPONSE_MESSAGE, ((SendMessage) response).getText());
	}

	@Test
	public void handleException() {
		Update testUpdate = createTestCommandUpdate(Command.START);
		RuntimeException testException = new RuntimeException("Test exception message");

		doThrow(testException).when(commandService).process(TEST_USER_ID, testUpdate);

		GatewayRuntimeException exception = assertThrows(GatewayRuntimeException.class, () -> updateHandler.apply(testUpdate));
		assertEquals(testException, exception.getCause());
	}

	static Stream<Arguments> commandProvider() {
		return Stream.of(
				Arguments.of(createTestCommandUpdate(Command.START)),
				Arguments.of(createTestEditedCommandUpdate(Command.START))
		);
	}
}
