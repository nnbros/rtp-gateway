package com.github.nnbros.rtp.gateway.bot.action.provider;

import com.github.nnbros.rtp.gateway.bot.action.Action;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ActionProvider {
	Action retrieve(Update update);

}
