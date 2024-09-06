package com.github.nnbros.rtp.gateway.bot;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandService {

	BotApiMethod<?> processUpdate(Update update);
}
