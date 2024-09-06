package com.github.nnbros.rtp.gateway.bot;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.mockito.Mockito.*;

public class WebhookRemoverTest {

	@Test
	public void removeWebhook() throws TelegramApiException {
		TelegramClient mockedTelegramClient = mock(TelegramClient.class);
		WebhookRemover webhookRemover = new WebhookRemover(mockedTelegramClient);
		DeleteWebhook deleteWebhook = DeleteWebhook.builder()
				.dropPendingUpdates(false)
				.build();

		webhookRemover.run();

		verify(mockedTelegramClient, times(1)).execute(deleteWebhook);
	}
}
