package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.model.ActionProcessorType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@ConfigurationProperties(prefix = "actions")
public class Actions {
	private Map<String, TreeSet<ActionProcessorType>> mapping;
	private Set<String> allowedForUnregisteredUsers;
}
