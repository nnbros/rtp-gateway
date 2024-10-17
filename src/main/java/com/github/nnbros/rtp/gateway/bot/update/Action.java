package com.github.nnbros.rtp.gateway.bot.update;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.meta.api.objects.Update;

public record Action(
		@NonNull String actionId,
		@Nullable String data,
		@NonNull Update update,
		@NonNull Long userId
) {
}
