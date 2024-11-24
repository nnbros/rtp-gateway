package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

import static com.github.nnbros.rtp.gateway.model.Events.*;
import static com.github.nnbros.rtp.gateway.model.States.*;

@Component
@EnableStateMachine
@RequiredArgsConstructor
public class StateMachineConfiguration extends EnumStateMachineConfigurerAdapter<States, Events> {
	private final Action<States, Events> registrationEventAction;
	private final Action<States, Events> errorEventAction;

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
	public void configure(final StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
		transitions
				.withExternal()
				.source(NEW)
				.target(START_REGISTRATION)
				.event(storyteller_create_char_start)
				.action(registrationEventAction, errorEventAction)

				.and()
				.withExternal()
				.source(START_REGISTRATION)
				.target(REGISTRATION_IN_PROGRESS)
				.event(storyteller_create_char_gender)
				.action(registrationEventAction, errorEventAction)

				.and()
				.withExternal()
				.source(REGISTRATION_IN_PROGRESS)
				.target(REGISTRATION_IN_PROGRESS)
				.event(storyteller_create_char_name)
				.action(registrationEventAction, errorEventAction)

				.and()
				.withExternal()
				.source(REGISTRATION_IN_PROGRESS)
				.target(REGISTRATION_IN_PROGRESS)
				.event(storyteller_create_char_class_selection)
				.action(registrationEventAction, errorEventAction)

				.and()
				.withExternal()
				.source(REGISTRATION_IN_PROGRESS)
				.target(REGISTRATION_IN_PROGRESS)
				.event(storyteller_create_char_class_confirmation)
				.action(registrationEventAction, errorEventAction)

				.and()
				.withExternal()
				.source(REGISTRATION_IN_PROGRESS)
				.target(REGISTRATION_COMPLETED)
				.event(storyteller_create_char_registration)
				.action(registrationEventAction, errorEventAction);
	}


}
