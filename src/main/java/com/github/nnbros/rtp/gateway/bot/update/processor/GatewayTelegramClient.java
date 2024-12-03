package com.github.nnbros.rtp.gateway.bot.update.processor;

import com.github.nnbros.rtp.gateway.bot.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayTelegramClient {
	private final TelegramClient client;
	private final MessageBuilder messageBuilder;

	@SneakyThrows
	public void send(Long userId, String callbackQueryId, String text) {
		if (callbackQueryId != null) {
			AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
					.callbackQueryId(callbackQueryId)
					.text(text)
					.showAlert(false)
					.build();
			client.execute(answer);
		} else {
			client.execute(messageBuilder.createMessage(userId, text));
		}
		log.info("Message was sent to user [{}] with text: {}", userId, text);
	}
}
