package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Getter
@RequiredArgsConstructor
public enum Command {
	START("Вы уже зарегистрированы."),
	HELP("Здесь когда-нибудь будет справка по боту."),
	UNKNOWN("Не знаю такой команды :(");

	private static final String COMMAND_PREFIX = "/";
	private final String commandResponse;

	public static Command parseCommand(Message message) {
		try {
			if (!message.hasText()) {
				throw new GatewayRuntimeException("Unable to parse command, message text is empty");
			}
			String commandText = message.getText().replaceFirst(COMMAND_PREFIX, "");
			return Command.valueOf(commandText.toUpperCase());
		} catch (IllegalArgumentException e) {
			return Command.UNKNOWN;
		}
	}
}
