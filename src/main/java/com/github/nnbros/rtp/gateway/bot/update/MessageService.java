package com.github.nnbros.rtp.gateway.bot.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class MessageService implements UpdateService {

	@Override
	public BotApiMethod<?> process(Long userId, Update update) {
		return null;
	}

	@Override
	public String retrieveActionData(Update update) {
		// returns null because telegram don't send anything on message
		return null;
	}

	@Override
	public String getUsername(Update update) {
		return "message";
	}
}
