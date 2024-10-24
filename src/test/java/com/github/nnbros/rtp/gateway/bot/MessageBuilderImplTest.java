package com.github.nnbros.rtp.gateway.bot;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.TEST_USER_ID;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

class MessageBuilderImplTest {

	private final MessageBuilderImpl messageBuilder = new MessageBuilderImpl();

	@Test
	void createMessage() {
		String text = "Hello, World!";

		SendMessage result = messageBuilder.createMessage(TEST_USER_ID, text);

		assertNotNull(result);
		assertEquals(text, result.getText());
	}
}

