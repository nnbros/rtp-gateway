package com.github.nnbros.rtp.gateway.bot.update;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ActionService implements UpdateService {

	@Override
	public BotApiMethod<?> process(Long userId, Update update) {
		CallbackQuery callbackQuery = update.getCallbackQuery();
		Objects.requireNonNull(callbackQuery, "Callback cannot be empty");
		return null;
	}

	@Override
	public String retrieveActionData(Update update) {
		return update.getCallbackQuery().getData();
	}

	@Override
	public String getUsername(Update update) {
		return update.getCallbackQuery().getFrom().getUserName();
	}
}
