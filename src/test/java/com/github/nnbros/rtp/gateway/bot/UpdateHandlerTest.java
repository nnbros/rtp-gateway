package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.createTestCommandUpdate;
import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.createTestMessageUpdate;
import static com.github.nnbros.rtp.gateway.bot.UpdateHandler.UNKNOWN_UPDATE_RESPONSE_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateHandlerTest {
	private final MessageBuilder messageBuilder = new MessageBuilderImpl();
	private final CommandService commandService = mock(CommandService.class);
	private final UpdateHandler updateHandler = new UpdateHandler(commandService, messageBuilder);

	@Test
	public void handleCommandUpdate() {
		Update testUpdate = createTestCommandUpdate(Command.START);
		BotApiMethod<?> testResponse = SendMessage.builder()
				.text("Test handleCommandUpdate")
				.chatId(testUpdate.getMessage().getChatId())
				.build();
		doReturn(testResponse).when(commandService).processUpdate(testUpdate);

		BotApiMethod<?> response = updateHandler.apply(testUpdate);

		verify(commandService, times(1)).processUpdate(testUpdate);
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
		doThrow(testException).when(commandService).processUpdate(testUpdate);

		GatewayRuntimeException exception = assertThrows(GatewayRuntimeException.class, () -> updateHandler.apply(testUpdate));
		assertEquals(testException, exception.getCause());
	}
}
