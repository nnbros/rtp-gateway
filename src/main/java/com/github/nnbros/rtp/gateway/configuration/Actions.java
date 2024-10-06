package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.model.ClientType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ConfigurationProperties(prefix = "actions")
public class Actions {
	private Map<String, ClientType> mapping;
	private Set<String> allowedForUnregisteredUsers;
}
