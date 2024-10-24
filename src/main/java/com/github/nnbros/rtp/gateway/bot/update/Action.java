package com.github.nnbros.rtp.gateway.bot.update;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public record Action(
		@NonNull String actionId,
		@Nullable String data,
		@NonNull Update update,
		@NonNull Long userId
) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Action action = (Action) o;
		return Objects.equals(actionId, action.actionId) && Objects.equals(data, action.data) && Objects.equals(userId, action.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(actionId, data, userId);
	}
}
