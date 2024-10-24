package com.github.nnbros.rtp.gateway.util;

import com.github.nnbros.rtp.gateway.bot.update.UpdateType;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BotUtilsTest {

	@ParameterizedTest
	@EnumSource(value = UpdateType.class, names = {"MESSAGE", "COMMAND"})
	void getUserIdForMessage(UpdateType updateType) {
		Update update = createTestMessageUpdate();

		Long userId = BotUtils.getUserId(update, updateType);

		assertEquals(TEST_USER_ID, userId);
	}

	@ParameterizedTest
	@EnumSource(value = UpdateType.class, names = {"EDITED_MESSAGE", "EDITED_COMMAND"})
	void getUserIdForEditedMessage(UpdateType updateType) {
		Update update = createTestEditedMessageUpdate();

		Long userId = BotUtils.getUserId(update, updateType);

		assertEquals(TEST_USER_ID, userId);
	}

	@Test
	void throwExceptionForUnsupportedUpdateType() {
		Update update = createTestEmptyUpdate();

		assertThrows(GatewayRuntimeException.class, () -> BotUtils.getUserId(update, UpdateType.UNKNOWN));
	}

	@ParameterizedTest
	@EnumSource(value = UpdateType.class, names = {"MESSAGE", "EDITED_MESSAGE"})
	void throwExceptionWhenMessageIsNull(UpdateType updateType) {
		Update update = createTestEmptyUpdate();

		assertThrows(NullPointerException.class, () -> BotUtils.getUserId(update, updateType));
	}
}

