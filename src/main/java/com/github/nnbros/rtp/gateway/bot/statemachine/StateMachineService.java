package com.github.nnbros.rtp.gateway.bot.statemachine;

import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class StateMachineService {
	private final StateMachineFactory<States, Events> stateMachineFactory;
	private final Map<String, StateMachine<States, Events>> userStateMachines = new ConcurrentHashMap<>();

	public StateMachine<States, Events> getStateMachineForUser(String userId) {
		return userStateMachines.computeIfAbsent(userId, id -> {
			StateMachine<States, Events> stateMachine = stateMachineFactory.getStateMachine(id);
			stateMachine.start();
			return stateMachine;
		});
	}

	public void removeStateMachineForUser(String userId) {
		StateMachine<States, Events> stateMachine = userStateMachines.remove(userId);
		if (stateMachine != null) {
			stateMachine.stop();
		}
	}
}
