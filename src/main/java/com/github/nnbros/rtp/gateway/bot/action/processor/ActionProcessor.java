package com.github.nnbros.rtp.gateway.bot.action.processor;

import com.github.nnbros.rtp.gateway.bot.action.Action;
import com.github.nnbros.rtp.gateway.model.ActionProcessorType;
import org.jetbrains.annotations.NotNull;

public interface ActionProcessor extends Comparable<ActionProcessor> {
	void process(Action action);

	ActionProcessorType getType();

	int getOrder();

	@Override
	default int compareTo(@NotNull ActionProcessor o) {
		return Integer.compare(this.getOrder(), o.getOrder());
	}

}
