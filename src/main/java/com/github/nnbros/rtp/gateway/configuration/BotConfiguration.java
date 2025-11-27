package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.common.autoconfigure.RtpBotProperties;
import com.github.nnbros.rtp.gateway.bot.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
	private final GatewayProperties gatewayProperties;
	private final RtpBotProperties rtpBotProperties;
	private static final Runnable WEBHOOK_RUNNABLE_STUB = () -> {
	};

	@Bean
	public SpringTelegramWebhookBot webhookBot(Runnable webhookSetter,
											   Runnable webhookRemover,
											   UpdateHandler updateHandler) {
		return SpringTelegramWebhookBot.builder()
				.setWebhook(webhookSetter)
				.deleteWebhook(webhookRemover)
				.updateHandler(updateHandler)
				.botPath(rtpBotProperties.getWebhookPath())
				.build();
	}

	@Bean
	public TelegramClient telegramClient() {
		return new OkHttpTelegramClient(rtpBotProperties.getToken());
	}

	@Bean
	@ConditionalOnProperty(name = "rtp-bot.webhook-enabled", havingValue = "false")
	public Runnable webhookSetter() {
		return WEBHOOK_RUNNABLE_STUB;
	}

	@Bean
	@ConditionalOnProperty(name = "rtp-bot.webhook-enabled", havingValue = "false")
	public Runnable webhookRemover() {
		return WEBHOOK_RUNNABLE_STUB;
	}

}
