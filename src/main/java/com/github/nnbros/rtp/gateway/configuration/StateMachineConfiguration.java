package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.bot.statemachine.ErrorAction;
import com.github.nnbros.rtp.gateway.bot.statemachine.RegistrationAction;
import com.github.nnbros.rtp.gateway.bot.update.ActionRouter;
import com.github.nnbros.rtp.gateway.bot.update.processor.GatewayTelegramClient;
import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;

import static com.github.nnbros.rtp.gateway.model.Events.*;
import static com.github.nnbros.rtp.gateway.model.States.*;

@Component
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfiguration extends EnumStateMachineConfigurerAdapter<States, Events> {
	private final ActionRouter actionRouter;
	private final GatewayTelegramClient errorProcessor;

	@Override
	public void configure(StateMachineConfigurationConfigurer<States, Events> config)
			throws Exception {
		config
				.withConfiguration()
				.autoStartup(true);
	}

	@Override
	public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
		states
				.withStates()
				.initial(NEW)
				.end(REGISTRATION_COMPLETED)
				.states(EnumSet.allOf(States.class));

	}

	@Override
	public void configure(final StateMachineTransitionConfigurer<States, Events> transitions) {
		configureTransition(transitions, NEW, START_REGISTRATION, storyteller_create_char_start);
		configureTransition(transitions, START_REGISTRATION, REGISTRATION_IN_PROGRESS, storyteller_create_char_gender);
		configureTransition(transitions, REGISTRATION_IN_PROGRESS, REGISTRATION_IN_PROGRESS,
				List.of(storyteller_create_char_start, storyteller_create_char_name, storyteller_create_char_class_selection, storyteller_create_char_class_confirmation));
		configureTransition(transitions, REGISTRATION_IN_PROGRESS, REGISTRATION_COMPLETED, storyteller_create_char_registration);
	}

	private void configureTransition(StateMachineTransitionConfigurer<States, Events> transitions,
									 States source,
									 States target,
									 List<Events> events) {
		events.forEach(event -> configureTransition(transitions, source, target, event));
	}

	@SneakyThrows

	private void configureTransition(StateMachineTransitionConfigurer<States, Events> transitions,
									 States source,
									 States target,
									 Events event) {
		transitions
				.withExternal()
				.source(source)
				.target(target)
				.event(event)
				.action(registrationEventAction(actionRouter), errorEventAction(errorProcessor));
	}

	@Bean
	public Action<States, Events> registrationEventAction(ActionRouter actionRouter) {
		return new RegistrationAction(actionRouter);
	}

	@Bean
	public Action<States, Events> errorEventAction(GatewayTelegramClient errorProcessor) {
		return new ErrorAction(errorProcessor);
	}
}
