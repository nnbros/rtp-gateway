package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
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
public class CommandServiceImpl implements CommandService {
	private final MessageBuilder messageBuilder;

	@Override
	public BotApiMethod<?> processUpdate(Update update) {
		Message message = update.getMessage();
		Objects.requireNonNull(message, "Message cannot be empty");
		if (!message.isCommand()) {
			throw new GatewayRuntimeException("Unable to process the update %s. It's not a command".formatted(update.getUpdateId()));
		}

		String chatId = message.getChatId().toString();
		Command command = parseCommand(message);
		log.debug("Starting to process command [{}]", command);
		return switch (command) {
			case START -> messageBuilder.createMessage(chatId, START.getCommandResponse());
			case HELP -> messageBuilder.createMessage(chatId, HELP.getCommandResponse());
			case UNKNOWN -> messageBuilder.createMessage(chatId, UNKNOWN.getCommandResponse());
		};
	}
}
