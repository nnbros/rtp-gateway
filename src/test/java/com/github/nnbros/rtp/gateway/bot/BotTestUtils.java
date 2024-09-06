package com.github.nnbros.rtp.gateway.bot;

import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

public class BotTestUtils {
	public static final int TEST_UPDATE_ID = 456;
	public static final String TEST_TEXT = "util";
	public static final long TEST_CHAT_ID = 123L;
	public static final String CHAT_PRIVATE_TYPE = "private";
	public static final String COMMAND_MESSAGE_TYPE = "bot_command";

	public static Message createTestMessage() {
		return createTestMessage(TEST_TEXT);
	}

	public static Message createTestMessage(String text) {
		Chat chat = Chat.builder()
				.type(CHAT_PRIVATE_TYPE)
				.id(TEST_CHAT_ID)
				.build();
		User from = User.builder()
				.id(TEST_CHAT_ID)
				.firstName("TestUser")
				.isBot(false)
				.build();
		return Message.builder()
				.text(text)
				.chat(chat)
				.from(from)
				.build();
	}

	public static Message createTestCommandMessage(String text) {
		MessageEntity messageEntity = MessageEntity.builder()
				.length(1)
				.offset(0)
				.type(COMMAND_MESSAGE_TYPE)
				.build();
		Message message = createTestMessage(text);
		message.setEntities(List.of(messageEntity));
		return message;
	}

	public static Update createTestEmptyUpdate() {
		Update update = new Update();
		update.setUpdateId(TEST_UPDATE_ID);
		return update;
	}

	public static Update createTestMessageUpdate() {
		Update update = new Update();
		update.setMessage(createTestMessage());
		update.setUpdateId(TEST_UPDATE_ID);
		return update;
	}

	public static Update createTestMessageUpdate(Message message) {
		Update update = createTestMessageUpdate();
		update.setMessage(message);
		return update;
	}

	public static Update createTestEditedMessageUpdate() {
		Update update = new Update();
		update.setEditedMessage(createTestMessage());
		update.setUpdateId(TEST_UPDATE_ID);
		return update;
	}

	public static Update createTestCommandUpdate(Command command) {
		Update update = createTestMessageUpdate();
		update.setMessage(createTestCommandMessage("/" + command.name().toLowerCase()));
		return update;
	}
}
