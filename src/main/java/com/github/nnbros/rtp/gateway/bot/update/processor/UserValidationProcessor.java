package com.github.nnbros.rtp.gateway.bot.update.processor;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.update.Action;
import com.github.nnbros.rtp.gateway.configuration.Actions;
import com.github.nnbros.rtp.gateway.exception.UpdateRuntimeException;
import com.github.nnbros.rtp.gateway.model.ActionProcessorType;
import com.github.nnbros.rtp.gateway.util.ActionError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidationProcessor implements ActionProcessor {
	private final UserService userService;
	private final Actions actions;

	@Override
	public void process(Action action) {
		if (actions.getAllowedForUnregisteredUsers().contains(action.actionId())) {
			if (userService.exists(action.userId())) {
				throw new UpdateRuntimeException(ActionError.ERROR_USER_ALREADY_REGISTERED.getText());
			}
		} else if (!userService.exists(action.userId())) {
			throw new UpdateRuntimeException(ActionError.ERROR_USER_NOT_REGISTERED.getText());
		}

	}

	@Override
	public ActionProcessorType getType() {
		return ActionProcessorType.USER_VALIDATOR;
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
