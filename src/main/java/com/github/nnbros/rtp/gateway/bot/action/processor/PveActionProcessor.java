package com.github.nnbros.rtp.gateway.bot.action.processor;

import com.github.nnbros.rtp.gateway.bot.action.Action;
import com.github.nnbros.rtp.gateway.client.PveServiceClient;
import com.github.nnbros.rtp.gateway.model.ActionProcessorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PveActionProcessor implements ActionProcessor {

	private final PveServiceClient pveServiceClient;

	@Override
	public void process(Action action) {
		pveServiceClient.sendAction(action.getActionId(), action.getData(), action.getUpdate());
	}

	@Override
	public ActionProcessorType getType() {
		return ActionProcessorType.PVE;
	}

	@Override
	public int getOrder() {
		return 3;
	}


}
