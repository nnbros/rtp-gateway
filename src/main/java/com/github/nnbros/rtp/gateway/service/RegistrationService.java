package com.github.nnbros.rtp.gateway.service;

import com.github.nnbros.rtp.gateway.bot.MessageBuilder;
import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nnbros.rtp.gateway.bot.Command.START;

@Service
@RequiredArgsConstructor
public class RegistrationService {
	public static final String STORYTELLER_CREATE_CHAR_START = "storyteller_create_char_start";
	private final MessageBuilder messageBuilder;
	private final UserService userService;
	private final StorytellerServiceClient storytellerServiceClient;

	public SendMessage startRegistration(Long userId, Update update) {
		if (userService.exists(userId)) {
			return messageBuilder.createMessage(userId, START.getCommandResponse());
		} else {
			storytellerServiceClient.sendAction(STORYTELLER_CREATE_CHAR_START, update);
			return null;
		}
	}
}
