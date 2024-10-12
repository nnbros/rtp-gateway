package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.bot.update.CommandService;
import com.github.nnbros.rtp.gateway.bot.update.UpdateService;
import com.github.nnbros.rtp.gateway.bot.update.UpdateType;
import com.github.nnbros.rtp.gateway.bot.update.ActionService;
import com.github.nnbros.rtp.gateway.bot.update.MessageService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.EnumMap;

import static com.github.nnbros.rtp.gateway.bot.update.UpdateType.*;

@Configuration
@EnableScheduling
@EnableConfigurationProperties({GatewayProperties.class, Actions.class})
public class GatewayConfiguration {
	@Bean
	public EnumMap<UpdateType, UpdateService> updateServiceMap(ActionService actionService,
															   CommandService commandService,
															   MessageService messageService) {
		EnumMap<UpdateType, UpdateService> enumMap = new EnumMap<>(UpdateType.class);
		enumMap.put(CALLBACK_QUERY, actionService);
		enumMap.put(COMMAND, commandService);
		enumMap.put(EDITED_COMMAND, commandService);
		enumMap.put(MESSAGE, messageService);
		return enumMap;
	}
}
