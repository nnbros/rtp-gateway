package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.bot.MessageBuilder;
import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

import static com.github.nnbros.rtp.gateway.bot.Command.START;

@Service
@RequiredArgsConstructor
public class RegistrationService {

	private final MessageBuilder messageBuilder;
	private final UserService userService;
	private final StorytellerServiceClient storytellerServiceClient;
	public static final String STORYTELLER_CREATE_CHAR_START = "storyteller_create_char_start";

	public SendMessage startRegistration(Long userId, Update update) {
		if (userService.exists(userId)) {
			return messageBuilder.createMessage(userId, START.getCommandResponse());
		} else {
			storytellerServiceClient.sendAction(STORYTELLER_CREATE_CHAR_START, null, update);
			userService.updateLastAction(userId, STORYTELLER_CREATE_CHAR_START);
			return null;
		}
	}

	@Scheduled(initialDelayString = "${gateway.user.unregistered.ttl}", fixedRateString = "${gateway.user.unregistered.ttl}")
	public void removeUnregisteredUsers() {
		Set<Long> usersToDelete = userService.usersToDelete();
		usersToDelete.forEach(userService::remove);
		storytellerServiceClient.unregisterUsers(usersToDelete);
	}
}
