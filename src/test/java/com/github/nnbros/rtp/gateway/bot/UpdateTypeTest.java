package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.bot.update.UpdateType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Stream;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.*;
import static com.github.nnbros.rtp.gateway.bot.update.UpdateType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateTypeTest {

	@ParameterizedTest
	@MethodSource("provideUpdates")
	public void getUpdateType(Update update, UpdateType expectedUpdateType) {
		UpdateType updateType = UpdateType.getUpdateType(update);

		assertEquals(expectedUpdateType, updateType);
	}

	private static Stream<Arguments> provideUpdates() {
		return Stream.of(
				Arguments.of(createTestCommandUpdate(Command.HELP), COMMAND),
				Arguments.of(createTestEditedCommandUpdate(Command.HELP), EDITED_COMMAND),
				Arguments.of(createTestMessageUpdate(), MESSAGE),
				Arguments.of(createTestEditedMessageUpdate(), EDITED_MESSAGE),
				Arguments.of(createTestEmptyUpdate(), UNKNOWN)
		);
	}
}
