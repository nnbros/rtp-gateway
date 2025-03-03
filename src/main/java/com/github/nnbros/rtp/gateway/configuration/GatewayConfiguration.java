package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.update.UpdateType;
import com.github.nnbros.rtp.gateway.bot.update.processor.ActionProcessor;
import com.github.nnbros.rtp.gateway.bot.update.provider.ActionProvider;
import com.github.nnbros.rtp.gateway.bot.update.provider.CallbackQueryActionProvider;
import com.github.nnbros.rtp.gateway.bot.update.provider.CommandActionProvider;
import com.github.nnbros.rtp.gateway.bot.update.provider.MessageActionProvider;
import com.github.nnbros.rtp.gateway.model.ActionProcessorType;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.nnbros.rtp.gateway.bot.update.UpdateType.*;

@Configuration
@EnableScheduling
@EnableConfigurationProperties({GatewayProperties.class, Actions.class, Messages.class})
@EntityScan({"com.github.nnbros.rtp.gateway.*", "org.springframework.statemachine.data.*"})
public class GatewayConfiguration {
	@Bean
	public EnumMap<UpdateType, ActionProvider> updateServiceMap(CallbackQueryActionProvider actionService,
																CommandActionProvider commandService,
																MessageActionProvider messageService) {
		EnumMap<UpdateType, ActionProvider> enumMap = new EnumMap<>(UpdateType.class);
		enumMap.put(CALLBACK_QUERY, actionService);
		enumMap.put(COMMAND, commandService);
		enumMap.put(EDITED_COMMAND, commandService);
		enumMap.put(MESSAGE, messageService);
		return enumMap;
	}

	@Bean
	public Map<String, List<ActionProcessor>> actionProcessors(Actions actions,
															   Collection<ActionProcessor> processors,
															   UserService userService) {
		TreeSet<ActionProcessor> sortedProcessors = new TreeSet<>(processors);
		return actions.getMapping()
				.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> getProcessors(entry.getValue(), sortedProcessors)));
	}

	private List<ActionProcessor> getProcessors(Collection<ActionProcessorType> processorTypes, TreeSet<ActionProcessor> actionProcessors) {
		return actionProcessors.stream()
				.filter(processor -> processorTypes.contains(processor.getType()))
				.toList();
	}
}
