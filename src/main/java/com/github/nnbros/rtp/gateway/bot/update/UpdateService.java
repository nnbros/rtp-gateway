package com.github.nnbros.rtp.gateway.bot.update;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateService {

	BotApiMethod<?> process(Long userId, Update update);

	String retrieveActionData(Update update);

	String getUsername(Update update);
}
