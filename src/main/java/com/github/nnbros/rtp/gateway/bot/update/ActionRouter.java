package com.github.nnbros.rtp.gateway.bot.update;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.update.processor.ActionProcessor;
import com.github.nnbros.rtp.gateway.bot.update.processor.DefaultActionProcessor;
import com.github.nnbros.rtp.gateway.configuration.Actions;
import com.github.nnbros.rtp.gateway.util.ActionError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class ActionRouter {
	private final UserService userService;
	private final Map<String, List<ActionProcessor>> actionProcessors;
	private final DefaultActionProcessor defaultActionProcessor;
	private final Set<String> allowedActionsForUnregisteredUsers;

	public ActionRouter(UserService userService,
						Map<String, List<ActionProcessor>> actionProcessors,
						DefaultActionProcessor defaultActionProcessor,
						Actions actions) {
		this.userService = userService;
		this.actionProcessors = actionProcessors;
		this.defaultActionProcessor = defaultActionProcessor;
		allowedActionsForUnregisteredUsers = actions.getAllowedForUnregisteredUsers();
	}


	public void route(Action action) {
		String actionId = action.actionId();

		retrieveErrorAction(action)
				.ifPresentOrElse(defaultActionProcessor::process, () ->
						actionProcessors.get(actionId)
								.forEach(processor -> processor.process(action)));
	}

	private Optional<Action> retrieveErrorAction(Action action) {
		String actionId = action.actionId();
		Update update = action.update();
		Long userId = action.userId();
		if (actionProcessors.containsKey(actionId)) {
			if (!allowedActionsForUnregisteredUsers.contains(actionId)) {
				return Optional.of(new Action(ActionError.ERROR_NOT_ALLOWED.name(), ActionError.ERROR_NOT_ALLOWED.getText(), update, userId));
			}
		} else if (ActionError.names().contains(actionId)) {
			return Optional.of(action);
		} else {
			return Optional.of(new Action(ActionError.ERROR_UNKNOWN.name(), ActionError.ERROR_UNKNOWN.getText(), update, userId));
		}
		if (userService.exists(userId)) {
			return Optional.of(new Action(ActionError.ERROR_USER_ALREADY_REGISTERED.name(), ActionError.ERROR_USER_ALREADY_REGISTERED.getText(), update, userId));
		}
		return Optional.empty();
	}
}
