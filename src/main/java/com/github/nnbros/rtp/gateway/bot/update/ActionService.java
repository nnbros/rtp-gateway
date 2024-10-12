package com.github.nnbros.rtp.gateway.bot.update;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import com.github.nnbros.rtp.gateway.configuration.Actions;
import com.github.nnbros.rtp.gateway.model.ClientType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class ActionService implements UpdateService {
	public static final String STORYTELLER_CREATE_CHAR_REGISTRATION = "storyteller_create_char_registration";
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
		CallbackQuery callbackQuery = update.getCallbackQuery();
		Objects.requireNonNull(callbackQuery, "Callback cannot be empty");
		String[] actionWithData = callbackQuery.getData().split(":");
		String actionId = actionWithData[0];
		String data = actionWithData.length > 1 ? actionWithData[1] : null;

		if (mapping.containsKey(actionId) && storytellerServiceClient.name() == mapping.get(actionId)) {
			if (allowedActionsForUnregisteredUsers.contains(actionId)) {
				if (STORYTELLER_CREATE_CHAR_REGISTRATION.equalsIgnoreCase(userService.getLastAction(userId))) {
					userService.create(userId, callbackQuery.getFrom().getUserName());
				}
				storytellerServiceClient.sendAction(actionId, data, update);
				userService.updateLastAction(userId, actionId);
			} else {
				log.error("The action is not allowed for unregistered users, actionId={}", actionId);
			}
		} else {
			log.error("Wrong input, actionId={}", actionId);
		}
		return null;
	}
}
