package com.github.nnbros.rtp.gateway.service;

import com.github.nnbros.rtp.gateway.bot.UpdateService;
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

	@Override
	public BotApiMethod<?> process(Long userId, Update update) {
		// get LastActionId by userId for unregistered user, the action before getName
		if (userService.getLastAction(userId).equalsIgnoreCase("storyteller_create_char_gender")) {
			storytellerServiceClient.sendAction("storyteller_create_char_name", update);
		}
		return null;
	}
}
