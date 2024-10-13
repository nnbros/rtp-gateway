package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.bot.update.UpdateType;
import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import com.github.nnbros.rtp.gateway.configuration.Actions;
import com.github.nnbros.rtp.gateway.model.ClientType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RegistrationService {
	private final UserService userService;
	private final StorytellerServiceClient storytellerServiceClient;
	private final Map<String, ClientType> mapping;
	private final Set<String> allowedActionsForUnregisteredUsers;
	public static final String STORYTELLER_CREATE_CHAR_START = "storyteller_create_char_start";
	public static final String STORYTELLER_CREATE_CHAR_GENDER = "storyteller_create_char_gender";
	public static final String STORYTELLER_CREATE_CHAR_NAME = "storyteller_create_char_name";
	public static final String STORYTELLER_CREATE_CHAR_REGISTRATION = "storyteller_create_char_registration";
	public static final String STORYTELLER_CREATE_CHAR_CANCEL = "storyteller_create_char_cancel";
	public static final String DELIMETER = ":";

	public RegistrationService(UserService userService,
							   StorytellerServiceClient storytellerServiceClient,
							   Actions actions) {
		this.userService = userService;
		this.storytellerServiceClient = storytellerServiceClient;
		mapping = actions.getMapping();
		allowedActionsForUnregisteredUsers = actions.getAllowedForUnregisteredUsers();
	}

	public SendMessage register(Long userId, Update update, String actionData, String username) {
		if (Set.of(UpdateType.COMMAND, UpdateType.EDITED_COMMAND).contains(UpdateType.getUpdateType(update))) { //it's a command
			updateAndSendAction(userId, STORYTELLER_CREATE_CHAR_START, null, update);
		} else if (actionData == null) { // it's a message
			if (Set.of(STORYTELLER_CREATE_CHAR_GENDER, STORYTELLER_CREATE_CHAR_NAME)
					.contains(userService.getLastAction(userId))) {
				updateAndSendAction(userId, STORYTELLER_CREATE_CHAR_NAME, null, update);
			}
		} else {
			String[] actionWithData = actionData.split(DELIMETER);
			String actionId = actionWithData[0];

			if (Set.of(STORYTELLER_CREATE_CHAR_START, STORYTELLER_CREATE_CHAR_CANCEL).contains(actionId)) {
				updateAndSendAction(userId, STORYTELLER_CREATE_CHAR_START, null, update);
			} else if (mapping.containsKey(actionId) && storytellerServiceClient.name() == mapping.get(actionId)) {
				if (allowedActionsForUnregisteredUsers.contains(actionId)) {
					String data = actionWithData.length > 1 ? actionWithData[1] : null;
					updateAndSendAction(userId, actionId, data, update);

					if (STORYTELLER_CREATE_CHAR_REGISTRATION.equalsIgnoreCase(actionId)) {
						userService.create(userId, username);
					}
				} else {
					log.error("The action is not allowed for unregistered users, actionId={}", actionId);
				}
			} else {
				log.error("Wrong input, actionId={}", actionId);
			}
		}
		return null;
	}

	private void updateAndSendAction(Long userId, String action, String data, Update update) {
		userService.updateLastAction(userId, action);
		storytellerServiceClient.sendAction(action, data, update);

	}

	@Scheduled(initialDelayString = "${gateway.user.unregistered.ttl}", fixedRateString = "${gateway.user.unregistered.ttl}")
	public void removeUnregisteredUsers() {
		Set<Long> usersToDelete = userService.usersToDelete();
		usersToDelete.forEach(userService::remove);
		log.info("Removed unregistered users with ids={} from cache", usersToDelete.stream()
				.map(Object::toString)
				.collect(Collectors.joining(",")));
		storytellerServiceClient.unregisterUsers(usersToDelete);
	}
}
