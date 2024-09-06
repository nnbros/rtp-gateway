package com.github.nnbros.rtp.gateway.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookRemover implements Runnable {
	private final TelegramClient telegramClient;

	@Override
	public void run() {
		try {
			log.info("Removing the bot webhook...");
			DeleteWebhook deleteWebhook = DeleteWebhook.builder()
					.dropPendingUpdates(false)
					.build();

			telegramClient.execute(deleteWebhook);
			log.info("Webhook has been successfully deleted");
		} catch (Exception e) {
			//TODO add retry logic
			log.error("Failed to remove webhook", e);
		}
	}
}
