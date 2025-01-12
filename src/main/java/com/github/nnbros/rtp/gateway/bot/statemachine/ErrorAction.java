package com.github.nnbros.rtp.gateway.bot.statemachine;

import com.github.nnbros.rtp.gateway.bot.update.LockService;
import com.github.nnbros.rtp.gateway.bot.update.processor.GatewayTelegramClient;
import com.github.nnbros.rtp.gateway.exception.UpdateRuntimeException;
import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import static com.github.nnbros.rtp.gateway.util.ActionError.ERROR_HELP;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ErrorAction implements Action<States, Events> {
	public static final String ERROR_PROCESSED_FLAG = "errorProcessed";
	public static final String ACTION = "ACTION";
	private final GatewayTelegramClient client;
	private final LockService lockService;

	@Override
	public void execute(StateContext<States, Events> context) {
		ExtendedState extendedState = context.getExtendedState();
		var action = extendedState.get(ACTION, com.github.nnbros.rtp.gateway.bot.update.Action.class);
		Long userId = action.getUserId();
		Exception exception = context.getException();
		String errorText = getErrorText(exception);
		extendedState.getVariables().put(ERROR_PROCESSED_FLAG, true);
		lockService.releaseLock(userId);
		log.warn("Send error after action failed: {}, {}", action.getActionId(), errorText);
		client.send(userId, null, exception.getMessage());
	}

	private String getErrorText(Exception exception) {
		if (exception instanceof UpdateRuntimeException) {
			return exception.getMessage();
		} else {
			return ERROR_HELP.getText();
		}
	}
}
