package com.github.nnbros.rtp.gateway.util;

import com.github.nnbros.rtp.gateway.bot.action.UpdateType;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.github.nnbros.rtp.gateway.bot.action.UpdateType.*;

public class BotUtils {

	public static Long getUserId(Update update, UpdateType updateType) {
		if (updateType == MESSAGE || updateType == COMMAND) {
			return getUserId(update.getMessage());
		} else if (updateType == EDITED_MESSAGE || updateType == EDITED_COMMAND) {
			return getUserId(update.getEditedMessage());
		} else if (updateType == CALLBACK_QUERY) {
			return getUserId(update.getCallbackQuery());
		} else {
			throw new GatewayRuntimeException("Unable to get the user id from update %s".formatted(update.getUpdateId()));
		}
	}

	public static String getUsername(Update update, UpdateType updateType) {
		if (updateType == MESSAGE || updateType == COMMAND) {
			return getUsername(update.getMessage());
		} else if (updateType == EDITED_MESSAGE || updateType == EDITED_COMMAND) {
			return getUsername(update.getEditedMessage());
		} else if (updateType == CALLBACK_QUERY) {
			return getUsername(update.getCallbackQuery());
		} else {
			throw new GatewayRuntimeException("Unable to get the username from update %s".formatted(update.getUpdateId()));
		}
	}

	private static Long getUserId(Message message) {
		return message.getFrom()
				.getId();
	}
	private static String getUsername(Message message) {
		return message.getFrom()
				.getUserName();
	}

	private static String getUsername(CallbackQuery callbackQuery) {
		return callbackQuery.getFrom()
				.getUserName();
	}

	private static Long getUserId(CallbackQuery callbackQuery) {
		return callbackQuery.getFrom()
				.getId();
	}
}
