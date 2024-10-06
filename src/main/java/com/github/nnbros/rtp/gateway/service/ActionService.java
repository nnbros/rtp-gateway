package com.github.nnbros.rtp.gateway.service;

import com.github.nnbros.rtp.gateway.bot.UpdateService;
import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import com.github.nnbros.rtp.gateway.configuration.Actions;
import com.github.nnbros.rtp.gateway.model.ClientType;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Set;

@Service
public class ActionService implements UpdateService {
	private final StorytellerServiceClient storytellerServiceClient;
	private final UserService userService;
	private final Map<String, ClientType> mapping;
	private final Set<String> allowedActionsForUnregisteredUsers;

	public ActionService(Actions actions,
						 StorytellerServiceClient storytellerServiceClient,
						 UserService userService) {
		this.storytellerServiceClient = storytellerServiceClient;
		this.userService = userService;
		mapping = actions.getMapping();
		allowedActionsForUnregisteredUsers = actions.getAllowedForUnregisteredUsers();
	}

	@Override
	public BotApiMethod<?> process(Long userId, Update update) {
		// todo check callback ?

		String actionId = update.getCallbackQuery().getData();

		if (mapping.containsKey(actionId) && storytellerServiceClient.name() == mapping.get(actionId)) {
			if (allowedActionsForUnregisteredUsers.contains(actionId)) {
				storytellerServiceClient.sendAction(actionId, update);
				if (userService.getLastAction(userId).equalsIgnoreCase("storyteller_create_char_registration")) {
					userService.create(userId, update.getCallbackQuery().getFrom().getUserName());
				}
			} else {
				// the action is not allowed for unregistered users
			}
		} else {
			// "неправильный ввод - сообщение"
		}

		return null;
	}
}
