package com.github.nnbros.rtp.gateway.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class MessageBuilderImpl implements MessageBuilder {

	@Override
	public SendMessage createMessage(Long userId, String text) {
		return SendMessage.builder()
				.chatId(userId)
				.text(text)
				.build();
	}
}
