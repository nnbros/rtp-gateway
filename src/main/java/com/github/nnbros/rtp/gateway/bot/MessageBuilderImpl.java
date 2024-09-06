package com.github.nnbros.rtp.gateway.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class MessageBuilderImpl implements MessageBuilder {

	@Override
	public SendMessage createMessage(String chatId, String text) {
		return SendMessage.builder()
				.chatId(chatId)
				.text(text)
				.build();
	}
}
