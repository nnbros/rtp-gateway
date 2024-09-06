package com.github.nnbros.rtp.gateway.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageBuilder {

	SendMessage createMessage(String chatId, String text);
}
