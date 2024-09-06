package com.github.nnbros.rtp.gateway.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

class MessageBuilderImplTest {

	private final MessageBuilderImpl messageBuilder = new MessageBuilderImpl();

	@Test
	void createMessage() {
		String chatId = "123456";
		String text = "Hello, World!";

		SendMessage result = messageBuilder.createMessage(chatId, text);

		assertNotNull(result);
		assertEquals(chatId, result.getChatId());
		assertEquals(text, result.getText());
	}
}

