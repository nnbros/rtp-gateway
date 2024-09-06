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
			} else {
				return MESSAGE;
			}
		} else if (update.hasEditedMessage()) {
			return EDITED_MESSAGE;
		} else {
			return UNKNOWN;
		}
	}
}
