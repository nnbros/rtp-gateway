package com.github.nnbros.rtp.gateway.bot.update.processor;

import com.github.nnbros.rtp.gateway.bot.MessageBuilder;
import com.github.nnbros.rtp.gateway.bot.update.Action;
import com.github.nnbros.rtp.gateway.bot.update.LockService;
import com.github.nnbros.rtp.gateway.util.ActionError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.TEST_USER_ID;

@ExtendWith(MockitoExtension.class)
class ErrorActionProcessorTest {
	@Mock
	private LockService lockService;
	@Mock
	private TelegramClient client;
	@Mock
	private MessageBuilder messageBuilder;
	@InjectMocks
	private ErrorActionProcessor processor;

	@Test
	void checkProcessOk() throws TelegramApiException {
		processor.process(new Action("ok", null, Mockito.mock(Update.class), TEST_USER_ID));
		Mockito.verify(lockService, Mockito.never()).releaseLock(ArgumentMatchers.anyLong());
		Mockito.verify(client, Mockito.never()).execute(ArgumentMatchers.any(SendMessage.class));
	}

	@Test
	void checkProcessError() throws TelegramApiException {
		Mockito.when(messageBuilder.createMessage(TEST_USER_ID, null)).thenReturn(new SendMessage("chat", "text"));

		processor.process(new Action(ActionError.ERROR_UNKNOWN.name(), null, Mockito.mock(Update.class), TEST_USER_ID));
		Mockito.verify(lockService, Mockito.times(1)).releaseLock(ArgumentMatchers.anyLong());
		Mockito.verify(client, Mockito.times(1)).execute(ArgumentMatchers.any(SendMessage.class));
	}

}
