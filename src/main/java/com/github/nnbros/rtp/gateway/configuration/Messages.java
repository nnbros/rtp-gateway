package com.github.nnbros.rtp.gateway.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ConfigurationProperties(prefix = "messages")
public class Messages implements InitializingBean {
	private Map<String, String> mapping;

	@Override
	public void afterPropertiesSet() {
		mapping = mapping.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
	}
}
