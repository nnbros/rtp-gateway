package com.github.nnbros.rtp.gateway.bot.update.provider;

import com.github.nnbros.rtp.gateway.bot.update.Action;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CallbackQueryActionProvider implements ActionProvider {
	public static final String DELIMETER = ":";
	public static final String STORYTELLER_CREATE_CHAR_START = "storyteller_create_char_start";
	public static final String STORYTELLER_CREATE_CHAR_CANCEL = "storyteller_create_char_cancel";

	@Override
	public Action retrieve(Update update) {
		CallbackQuery callbackQuery = update.getCallbackQuery();
		Objects.requireNonNull(callbackQuery, "Callback cannot be empty");

		String callbackData = callbackQuery.getData();
		String[] actionWithData = callbackData.split(DELIMETER);
		String actionId = actionWithData[0];
		String data = actionWithData.length > 1 ? actionWithData[1] : null;

		if (STORYTELLER_CREATE_CHAR_CANCEL.equalsIgnoreCase(actionId)) {
			actionId = STORYTELLER_CREATE_CHAR_START;
		}
		return new Action(actionId, data, update, callbackQuery.getFrom().getId());
	}
}
