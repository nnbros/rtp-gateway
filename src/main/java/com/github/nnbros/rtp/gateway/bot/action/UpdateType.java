package com.github.nnbros.rtp.gateway.bot.action;

import org.telegram.telegrambots.meta.api.objects.Update;

public enum UpdateType {
	COMMAND,
	EDITED_COMMAND,
	MESSAGE,
	EDITED_MESSAGE,
	CALLBACK_QUERY,
	UNKNOWN;

	public static UpdateType getUpdateType(Update update) {
		if (update.hasMessage()) {
			if (update.getMessage().isCommand()) {
				return COMMAND;
			}
			return MESSAGE;
		} else if (update.hasEditedMessage()) {
			if (update.getEditedMessage().isCommand()) {
				return EDITED_COMMAND;
			}
			return EDITED_MESSAGE;
		} else if (update.hasCallbackQuery()) {
			return CALLBACK_QUERY;
		}

		return UNKNOWN;
	}
}
