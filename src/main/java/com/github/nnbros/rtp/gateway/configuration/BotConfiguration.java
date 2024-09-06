package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.bot.UpdateHandler;
import com.github.nnbros.rtp.gateway.bot.WebhookRemover;
import com.github.nnbros.rtp.gateway.bot.WebhookSetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot;

@Configuration
public class BotConfiguration {

	@Bean
	public SpringTelegramWebhookBot webhookBot(GatewayProperties properties,
											   WebhookSetter webhookSetter,
											   WebhookRemover webhookRemover,
											   UpdateHandler updateHandler) {
		return SpringTelegramWebhookBot.builder()
				.setWebhook(webhookSetter)
				.deleteWebhook(webhookRemover)
				.updateHandler(updateHandler)
				.botPath(properties.getRtpBot().getWebhookPath())
				.build();
	}

	@Bean
	public TelegramClient telegramClient(GatewayProperties properties) {
		return new OkHttpTelegramClient(properties.getRtpBot().getToken());
	}

}
