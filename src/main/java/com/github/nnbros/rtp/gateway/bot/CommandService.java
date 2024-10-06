package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import com.github.nnbros.rtp.gateway.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Objects;

import static com.github.nnbros.rtp.gateway.bot.Command.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandService implements UpdateService {
	private final RegistrationService registrationService;
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
			case START -> registrationService.startRegistration(userId, update);
			case HELP -> messageBuilder.createMessage(userId, HELP.getCommandResponse());
			case UNKNOWN -> messageBuilder.createMessage(userId, UNKNOWN.getCommandResponse());
		};
	}
}
