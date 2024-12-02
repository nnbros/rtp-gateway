package com.github.nnbros.rtp.gateway.bot.statemachine;

import com.github.nnbros.rtp.gateway.bot.update.processor.ErrorActionProcessor;
import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ErrorAction implements Action<States, Events> {
	private final ErrorActionProcessor errorProcessor;

	@Override
	@SneakyThrows
	public void execute(StateContext<States, Events> context) {
		var action = context.getExtendedState().get("ACTION", com.github.nnbros.rtp.gateway.bot.update.Action.class);
		Long userId = action.userId();
		errorProcessor.process(userId, null, context.getException().getMessage());
	}
}
