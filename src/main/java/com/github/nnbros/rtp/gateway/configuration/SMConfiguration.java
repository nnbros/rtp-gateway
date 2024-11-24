package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.bot.statemachine.ErrorAction;
import com.github.nnbros.rtp.gateway.bot.statemachine.RegistrationAction;
import com.github.nnbros.rtp.gateway.bot.update.ActionRouter;
import com.github.nnbros.rtp.gateway.bot.update.processor.ErrorActionProcessor;
import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;

@Configuration
public class SMConfiguration {

	@Bean
	public Action<States, Events> registrationEventAction(ActionRouter actionRouter) {
		return new RegistrationAction(actionRouter);
	}

	@Bean
	public Action<States, Events> errorEventAction(ErrorActionProcessor errorProcessor) {
		return new ErrorAction(errorProcessor);
	}
}
