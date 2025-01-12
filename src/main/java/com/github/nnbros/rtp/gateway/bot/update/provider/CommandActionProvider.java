package com.github.nnbros.rtp.gateway.bot.update.provider;

import com.github.nnbros.rtp.gateway.bot.Command;
import com.github.nnbros.rtp.gateway.bot.update.Action;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Objects;

import static com.github.nnbros.rtp.gateway.bot.Command.parseCommand;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandActionProvider implements ActionProvider {
	@Override
	public Action retrieve(Update update) {
		Message message = update.hasMessage() ? update.getMessage() : update.getEditedMessage();
		Objects.requireNonNull(message, "Message cannot be empty");
		if (!message.isCommand()) {
			throw new GatewayRuntimeException("Unable to process the update %s. It's not a command".formatted(update.getUpdateId()));
		}

		Command command = parseCommand(message);
		log.debug("Starting to process command [{}]", command);
		return new Action(command.getAction(), command.getCommandResponse(), message.getFrom().getId(), update);
	}
}
