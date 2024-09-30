package com.github.nnbros.rtp.gateway.bot;

import org.telegram.telegrambots.meta.api.objects.Update;

public enum UpdateType {
	COMMAND,
	MESSAGE,
	EDITED_MESSAGE,
	UNKNOWN;

	public static UpdateType getUpdateType(Update update) {
		if (update.hasMessage()) {
			if (update.getMessage().isCommand()) {
				return COMMAND;
			}
			return MESSAGE;
		} else if (update.hasEditedMessage()) {
			if (update.getEditedMessage().isCommand()) {
				return COMMAND;
			}
			return EDITED_MESSAGE;
		}
		return UNKNOWN;
	}
}
