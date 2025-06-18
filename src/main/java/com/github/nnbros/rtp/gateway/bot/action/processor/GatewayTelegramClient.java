package com.github.nnbros.rtp.gateway.bot.action.processor;

import com.github.nnbros.rtp.gateway.bot.MessageBuilder;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayTelegramClient {
	private final TelegramClient client;
	private final MessageBuilder messageBuilder;

	public void send(Long userId, String callbackQueryId, String text) {
		try {
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
		} catch (TelegramApiException e) {
			log.error("Failed to send a message to a client with userId {}", userId, e);
		}
	}
}
