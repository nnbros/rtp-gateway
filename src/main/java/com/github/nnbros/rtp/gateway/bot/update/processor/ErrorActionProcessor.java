package com.github.nnbros.rtp.gateway.bot.update.processor;

import com.github.nnbros.rtp.gateway.bot.MessageBuilder;
import com.github.nnbros.rtp.gateway.bot.update.LockService;
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
	public void process(Long userId, String text) {
		lockService.releaseLock(userId);
		log.info("unlock user {}", userId);
		client.execute(messageBuilder.createMessage(userId, text));
	}
}
