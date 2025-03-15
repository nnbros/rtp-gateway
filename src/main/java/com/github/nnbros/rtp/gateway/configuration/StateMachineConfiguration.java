package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.bot.statemachine.ErrorAction;
import com.github.nnbros.rtp.gateway.bot.statemachine.RegistrationAction;
import com.github.nnbros.rtp.gateway.bot.update.ActionRouter;
import com.github.nnbros.rtp.gateway.bot.update.LockService;
import com.github.nnbros.rtp.gateway.bot.update.processor.GatewayTelegramClient;
import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

import java.util.EnumSet;
import java.util.List;

import static com.github.nnbros.rtp.gateway.model.Events.*;
import static com.github.nnbros.rtp.gateway.model.States.*;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfiguration extends EnumStateMachineConfigurerAdapter<States, Events> {
	private final ActionRouter actionRouter;
	private final GatewayTelegramClient errorProcessor;
	private final LockService lockService;
	private final JpaStateMachineRepository jpaStateMachineRepository;

	@Override
	public void configure(StateMachineConfigurationConfigurer<States, Events> config)
			throws Exception {
		config
				.withPersistence()
				.runtimePersister(stateMachineRuntimePersister(jpaStateMachineRepository));
	}

	@Override
	public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
		states
				.withStates()
				.initial(NEW)
				.end(END)
				.states(EnumSet.allOf(States.class));
	}

	@Override
	public void configure(final StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
		configureTransition(transitions, NEW, START_REGISTRATION, storyteller_create_char_start);
		configureTransition(transitions, START_REGISTRATION, REGISTRATION_IN_PROGRESS, storyteller_create_char_gender);
		configureTransition(transitions, REGISTRATION_IN_PROGRESS, REGISTRATION_IN_PROGRESS,
				List.of(storyteller_create_char_start, storyteller_create_char_gender, storyteller_create_char_name, storyteller_create_char_class_selection, storyteller_create_char_class_confirmation));
		configureTransition(transitions, REGISTRATION_IN_PROGRESS, REGISTRATION_COMPLETED, storyteller_create_char_registration);
		configureTransition(transitions, REGISTRATION_COMPLETED, REGISTRATION_COMPLETED,
				List.of(storyteller_main_menu,
						storyteller_main_menu_monster_hunt,
						storyteller_main_menu_character,
						storyteller_main_menu_character_details,
						storyteller_main_menu_character_deck_builder,
						storyteller_main_menu_character_class,
                        storyteller_main_menu_army
				));
		//todo break the statemachine status here
	}

	private void configureTransition(StateMachineTransitionConfigurer<States, Events> transitions,
									 States source,
									 States target,
									 List<Events> events) throws Exception {
		for (Events event : events) {
			configureTransition(transitions, source, target, event);
		}
	}

	private void configureTransition(StateMachineTransitionConfigurer<States, Events> transitions,
									 States source,
									 States target,
									 Events event) throws Exception {
		transitions
				.withExternal()
				.source(source)
				.target(target)
				.event(event)
				.action(registrationEventAction(actionRouter), errorEventAction(errorProcessor, lockService));
	}

	@Bean
	public Action<States, Events> registrationEventAction(ActionRouter actionRouter) {
		return new RegistrationAction(actionRouter);
	}

	@Bean
	public Action<States, Events> errorEventAction(GatewayTelegramClient errorProcessor,
												   LockService lockService) {
		return new ErrorAction(errorProcessor, lockService);
	}

	@Bean
	public StateMachineRuntimePersister<States, Events, String> stateMachineRuntimePersister(
			JpaStateMachineRepository jpaStateMachineRepository) {
		return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
	}

	@Bean
	public StateMachineService<States, Events> stateMachineService(StateMachineFactory<States, Events> stateMachineFactory,
																   StateMachinePersist<States, Events, String> stateMachinePersist) {
		return new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersist);
	}
}
