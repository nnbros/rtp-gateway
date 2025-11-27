package com.github.nnbros.rtp.gateway.bot.action.processor;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.action.Action;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.github.nnbros.rtp.gateway.BotTestUtils.TEST_USER_ID;

@ExtendWith(MockitoExtension.class)
class RegisterUserActionProcessorTest {
	@Mock
	private UserService userService;
	@InjectMocks
	private RegisterUserActionProcessor processor;

	@Test
	void checkProcessOk() {
		processor.process(new Action("ok", null, TEST_USER_ID, createUpdate()));
		Mockito.verify(userService, Mockito.times(1)).create(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString());
	}

	private Update createUpdate() {
		Update update = new Update();
		CallbackQuery callbackQuery = new CallbackQuery();
		User user = new User(TEST_USER_ID, "first", false);
		user.setUserName("username");
		callbackQuery.setFrom(user);
		update.setCallbackQuery(callbackQuery);
		return update;
	}
}
