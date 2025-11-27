package com.github.nnbros.rtp.gateway.bot.action.processor;

import com.github.nnbros.rtp.common.telegram.UpdateType;
import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.action.Action;
import com.github.nnbros.rtp.gateway.model.ActionProcessorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nnbros.rtp.common.util.BotUtils.getUsername;

@Component
@RequiredArgsConstructor
public class RegisterUserActionProcessor implements ActionProcessor {
	private final UserService userService;

	@Override
	public void process(Action action) {
		Update update = action.getUpdate();
		String username = getUsername(update, UpdateType.getUpdateType(update));
		userService.create(action.getUserId(), username);
	}

	@Override
	public ActionProcessorType getType() {
		return ActionProcessorType.REGISTRAR;
	}

	@Override
	public int getOrder() {
		return 4;
	}
}
