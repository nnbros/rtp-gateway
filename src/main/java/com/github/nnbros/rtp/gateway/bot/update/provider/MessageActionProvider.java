package com.github.nnbros.rtp.gateway.bot.update.provider;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.update.Action;
import com.github.nnbros.rtp.gateway.util.ActionError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MessageActionProvider implements ActionProvider {
	private final UserService userService;
	public static final String STORYTELLER_CREATE_CHAR_GENDER = "storyteller_create_char_gender";
	public static final String STORYTELLER_CREATE_CHAR_NAME = "storyteller_create_char_name";

	@Override
	public Action retrieve(Update update) {
		Message message = update.hasMessage() ? update.getMessage() : update.getEditedMessage();
		Long userId = message.getFrom().getId();

		String action = userService.getLastAction(userId)
				.filter(lastAction -> Set.of(STORYTELLER_CREATE_CHAR_GENDER, STORYTELLER_CREATE_CHAR_NAME).contains(lastAction))
				.map(s -> STORYTELLER_CREATE_CHAR_NAME)
				.orElse(ActionError.ERROR_UNKNOWN.name());
		return new Action(action, null, update, userId);
	}
}
