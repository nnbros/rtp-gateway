package com.github.nnbros.rtp.gateway.bot.update.provider;

import com.github.nnbros.rtp.gateway.bot.update.Action;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ActionProvider {
	Action retrieve(Update update);

}
