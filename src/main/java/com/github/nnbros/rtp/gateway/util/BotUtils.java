package com.github.nnbros.rtp.gateway.util;

import com.github.nnbros.rtp.gateway.bot.UpdateType;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.github.nnbros.rtp.gateway.bot.UpdateType.*;

public class BotUtils {

	public static String getUserId(Update update, UpdateType updateType) {
		if (updateType == MESSAGE || updateType == COMMAND) {
			return getUserId(update.getMessage());
		} else if (updateType == EDITED_MESSAGE) {
			return getUserId(update.getEditedMessage());
		} else {
			throw new GatewayRuntimeException("Unable to get the user id from update %s".formatted(update.getUpdateId()));
		}
	}

	private static String getUserId(Message message) {
		return message.getFrom()
				.getId()
				.toString();
	}
}
