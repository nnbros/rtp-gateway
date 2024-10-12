package com.github.nnbros.rtp.gateway.bot.registration;

import com.github.nnbros.rtp.gateway.bot.MessageBuilder;
import com.github.nnbros.rtp.gateway.bot.MessageBuilderImpl;
import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.TEST_USER_ID;
import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.createTestMessageUpdate;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
	@Spy
	private final MessageBuilder messageBuilder = new MessageBuilderImpl();
	@Mock
	private UserService userService;
	@Mock
	private StorytellerServiceClient storytellerServiceClient;
	@InjectMocks
	private RegistrationService registrationService;

	@Test
	void startRegistrationUserNotExists() {
		Mockito.when(userService.exists(TEST_USER_ID)).thenReturn(false);
		Update update = createTestMessageUpdate();
		SendMessage response = registrationService.startRegistration(TEST_USER_ID, update);
		Assertions.assertNull(response);
	}

	@Test
	void startRegistrationUserExists() {
		Mockito.when(userService.exists(TEST_USER_ID)).thenReturn(true);
		SendMessage response = registrationService.startRegistration(TEST_USER_ID, createTestMessageUpdate());
		assertInstanceOf(SendMessage.class, response);
	}
}