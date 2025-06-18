package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import com.github.nnbros.rtp.gateway.exception.UpdateRuntimeException;
import com.github.nnbros.rtp.gateway.util.ActionError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.github.nnbros.rtp.gateway.bot.action.provider.CallbackQueryActionProvider.STORYTELLER_CREATE_CHAR_START;


@Getter
@RequiredArgsConstructor
public enum Command {
	START(null, STORYTELLER_CREATE_CHAR_START),
	HELP(ActionError.ERROR_HELP.getText(), ActionError.ERROR_HELP.name()),
	UNKNOWN(ActionError.ERROR_UNKNOWN.getText(), ActionError.ERROR_UNKNOWN.name());

	private static final String COMMAND_PREFIX = "/";
	private final String commandResponse;
	private final String action;

	public static Command parseCommand(Message message) {
		try {
			if (!message.hasText()) {
				throw new GatewayRuntimeException("Unable to parse command, message text is empty");
			}
			String commandText = message.getText().replaceFirst(COMMAND_PREFIX, "");
			Command command = Command.valueOf(commandText.toUpperCase());
			if (command == START) {
				return command;
			} else {
				throw new UpdateRuntimeException(command.getCommandResponse(), message.getFrom().getId());
			}
		} catch (IllegalArgumentException e) {
			throw new UpdateRuntimeException(Command.UNKNOWN.getCommandResponse(), message.getFrom().getId());
		}
	}
}
