package com.github.nnbros.rtp.gateway.bot.update;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.update.processor.ActionProcessor;
import com.github.nnbros.rtp.gateway.configuration.Actions;
import com.github.nnbros.rtp.gateway.exception.UpdateRuntimeException;
import com.github.nnbros.rtp.gateway.util.ActionError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class ActionRouter {
	private final UserService userService;
	private final Map<String, List<ActionProcessor>> actionProcessors;
	private final Set<String> allowedActionsForUnregisteredUsers;

	public ActionRouter(UserService userService,
						Map<String, List<ActionProcessor>> actionProcessors,
						Actions actions) {
		this.userService = userService;
		this.actionProcessors = actionProcessors;
		allowedActionsForUnregisteredUsers = actions.getAllowedForUnregisteredUsers();
	}


	public void route(Action action) {
		String actionId = action.actionId();

		retrieveErrorAction(action);
		actionProcessors.get(actionId)
				.forEach(processor -> processor.process(action));
	}

	private void retrieveErrorAction(Action action) {
		String actionId = action.actionId();
		Long userId = action.userId();
		log.info("Routing action = {}, from user = {}", actionId, userId);
		if (!actionProcessors.containsKey(actionId)) {
			if (ActionError.names().contains(actionId)) {
				throw new UpdateRuntimeException(action.data());
			} else {
				throw new UpdateRuntimeException(ActionError.ERROR_UNKNOWN.getText());
			}
		}
	}
}
