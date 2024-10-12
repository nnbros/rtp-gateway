package com.github.nnbros.rtp.gateway.bot.update;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class MessageService implements UpdateService {
	private final UserService userService;
	private final StorytellerServiceClient storytellerServiceClient;
	public static final String STORYTELLER_CREATE_CHAR_GENDER = "storyteller_create_char_gender";
	public static final String STORYTELLER_CREATE_CHAR_NAME = "storyteller_create_char_name";

	@Override
	public BotApiMethod<?> process(Long userId, Update update) {
		// get LastActionId by userId for unregistered user, need to check if the action we have before getName
		if (STORYTELLER_CREATE_CHAR_GENDER.equalsIgnoreCase(userService.getLastAction(userId))) {
			storytellerServiceClient.sendAction(STORYTELLER_CREATE_CHAR_NAME, null, update);
			userService.updateLastAction(userId, STORYTELLER_CREATE_CHAR_NAME);
		}
		return null;
	}
}
