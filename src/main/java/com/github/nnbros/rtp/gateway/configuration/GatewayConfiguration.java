package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.bot.CommandService;
import com.github.nnbros.rtp.gateway.bot.UpdateService;
import com.github.nnbros.rtp.gateway.bot.UpdateType;
import com.github.nnbros.rtp.gateway.service.ActionService;
import com.github.nnbros.rtp.gateway.service.MessageService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;

import static com.github.nnbros.rtp.gateway.bot.UpdateType.*;

@Configuration
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
