package com.github.nnbros.rtp.gateway.bot.action;

import com.github.nnbros.rtp.gateway.bot.action.processor.ActionProcessor;
import com.github.nnbros.rtp.gateway.exception.UpdateRuntimeException;
import com.github.nnbros.rtp.gateway.util.ActionError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActionRouter {
	private final Map<String, List<ActionProcessor>> actionProcessors;

	public void route(Action action) {
		String actionId = action.getActionId();

		retrieveErrorAction(action);
		actionProcessors.get(actionId)
				.forEach(processor -> processor.process(action));
	}

	private void retrieveErrorAction(Action action) {
		String actionId = action.getActionId();
		Long userId = action.getUserId();
		log.info("Routing action = {}, from user = {}", actionId, userId);
		if (!actionProcessors.containsKey(actionId)) {
			if (ActionError.names().contains(actionId)) {
				throw new UpdateRuntimeException(action.getData());
			} else {
				throw new UpdateRuntimeException(ActionError.ERROR_UNKNOWN.getText());
			}
		}
	}
}
