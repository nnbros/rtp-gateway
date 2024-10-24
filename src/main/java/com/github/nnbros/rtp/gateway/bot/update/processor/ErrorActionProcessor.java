package com.github.nnbros.rtp.gateway.bot.update.processor;

import com.github.nnbros.rtp.gateway.bot.MessageBuilder;
import com.github.nnbros.rtp.gateway.bot.update.Action;
import com.github.nnbros.rtp.gateway.bot.update.LockService;
import com.github.nnbros.rtp.gateway.util.ActionError;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorActionProcessor {
	private final LockService lockService;
	private final TelegramClient client;
	private final MessageBuilder messageBuilder;

	@SneakyThrows
	public void process(Action action) {
		Long userId = action.userId();
		if (ActionError.names().contains(action.actionId())) {
			lockService.releaseLock(userId);
			log.info("unlock user {}", userId);
			client.execute(messageBuilder.createMessage(userId, action.data()));
		}
	}
}
