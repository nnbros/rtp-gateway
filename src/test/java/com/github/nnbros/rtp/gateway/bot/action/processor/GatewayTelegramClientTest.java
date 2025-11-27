package com.github.nnbros.rtp.gateway.bot.action.processor;

import com.github.nnbros.rtp.gateway.bot.MessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static com.github.nnbros.rtp.gateway.BotTestUtils.TEST_USER_ID;

@ExtendWith(MockitoExtension.class)
class GatewayTelegramClientTest {
	@Mock
	private TelegramClient client;
	@Mock
	private MessageBuilder messageBuilder;
	@InjectMocks
	private GatewayTelegramClient processor;

	@Test
	void checkProcessError() throws TelegramApiException {
		Mockito.when(messageBuilder.createMessage(TEST_USER_ID, null)).thenReturn(new SendMessage("chat", "text"));

		processor.send(TEST_USER_ID, null, null);
		Mockito.verify(client, Mockito.times(1)).execute(ArgumentMatchers.any(SendMessage.class));
	}

}
