package com.github.nnbros.rtp.gateway.configuration;

import com.github.nnbros.rtp.gateway.model.ActionProcessorType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@ConfigurationProperties(prefix = "actions")
public class Actions implements InitializingBean {
	private Map<String, TreeSet<ActionProcessorType>> mapping;
	private Set<String> allowedForUnregisteredUsers;

	@Override
	public void afterPropertiesSet() {
		mapping.forEach((action, processors) -> addMandatoryProcessors(processors));
	}

	private void addMandatoryProcessors(TreeSet<ActionProcessorType> processors) {
		processors.add(ActionProcessorType.USER_UPDATER);
		processors.add(ActionProcessorType.USER_VALIDATOR);
	}
}
