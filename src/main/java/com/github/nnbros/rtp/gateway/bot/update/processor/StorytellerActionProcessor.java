package com.github.nnbros.rtp.gateway.bot.update.processor;

import com.github.nnbros.rtp.gateway.bot.update.Action;
import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import com.github.nnbros.rtp.gateway.model.ActionProcessorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorytellerActionProcessor implements ActionProcessor {

	private final StorytellerServiceClient storytellerServiceClient;

	@Override
	public void process(Action action) {
		storytellerServiceClient.sendAction(action.actionId(), action.data(), action.update());
	}

	@Override
	public ActionProcessorType getType() {
		return ActionProcessorType.STORYTELLER;
	}

	@Override
	public int getOrder() {
		return 2;
	}


}
