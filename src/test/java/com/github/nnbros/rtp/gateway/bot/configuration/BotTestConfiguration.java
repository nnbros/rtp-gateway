package com.github.nnbros.rtp.gateway.bot.configuration;

import com.github.nnbros.rtp.gateway.configuration.GatewayProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class BotTestConfiguration {
	@Bean
	public GatewayProperties gatewayProperties() {
		return new GatewayProperties();
	}
}
