package com.github.nnbros.rtp.gateway.bot.update;

import com.github.nnbros.rtp.gateway.bot.Command;
import com.github.nnbros.rtp.gateway.bot.MessageBuilder;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Objects;

import static com.github.nnbros.rtp.gateway.bot.Command.*;
import static com.github.nnbros.rtp.gateway.bot.registration.RegistrationService.STORYTELLER_CREATE_CHAR_START;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandService implements UpdateService {
	private final MessageBuilder messageBuilder;

	@Override
	public BotApiMethod<?> process(Long userId, Update update) {
		Message message = update.hasMessage() ? update.getMessage() : update.getEditedMessage();
		Objects.requireNonNull(message, "Message cannot be empty");
		if (!message.isCommand()) {
			throw new GatewayRuntimeException("Unable to process the update %s. It's not a command".formatted(update.getUpdateId()));
		}

		Command command = parseCommand(message);
		log.debug("Starting to process command [{}]", command);
		return switch (command) {
			case START -> null;
			case HELP -> messageBuilder.createMessage(userId, HELP.getCommandResponse());
			case UNKNOWN -> messageBuilder.createMessage(userId, UNKNOWN.getCommandResponse());
		};
	}

	@Override
	public String retrieveActionData(Update update) {
		Message message = update.hasMessage() ? update.getMessage() : update.getEditedMessage();

		Command command = parseCommand(message);
		log.debug("Starting to process command [{}]", command);
		return switch (command) {
			case START -> STORYTELLER_CREATE_CHAR_START;
			case HELP, UNKNOWN -> "unknown";
		};
	}

	@Override
	public String getUsername(Update update) {
		return "command";
	}
}
