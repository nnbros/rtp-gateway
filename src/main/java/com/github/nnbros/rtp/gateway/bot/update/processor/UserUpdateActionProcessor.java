package com.github.nnbros.rtp.gateway.bot.update.processor;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.update.Action;
import com.github.nnbros.rtp.gateway.model.ActionProcessorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdateActionProcessor implements ActionProcessor {
	private final UserService userService;

	@Override
	public void process(Action action) {
		userService.updateLastAction(action.userId(), action.actionId());
	}

	@Override
	public ActionProcessorType getType() {
		return ActionProcessorType.USER_UPDATER;
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
