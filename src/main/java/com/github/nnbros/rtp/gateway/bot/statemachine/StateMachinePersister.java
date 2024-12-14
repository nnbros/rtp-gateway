package com.github.nnbros.rtp.gateway.bot.statemachine;

import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.HashMap;
import java.util.Map;

public class StateMachinePersister implements StateMachinePersist<States, Events, String> {
	private final Map<String, StateMachineContext<States, Events>> contexts = new HashMap<>();

	@Override
	public void write(StateMachineContext<States, Events> context, String contextObj) {
		contexts.put(contextObj, context);
	}

	@Override
	public StateMachineContext<States, Events> read(final String contextObj) {
		return contexts.get(contextObj);
	}
}
