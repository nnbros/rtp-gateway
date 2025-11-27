package com.github.nnbros.rtp.gateway.bot.action.processor;

import com.github.nnbros.rtp.gateway.bot.registration.UserService;
import com.github.nnbros.rtp.gateway.bot.action.Action;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nnbros.rtp.gateway.BotTestUtils.TEST_USER_ID;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class UserActionUpdateProcessorTest {
	@Mock
	private UserService userService;
	@InjectMocks
	private UserActionUpdateProcessor processor;

	@Test
	void checkProcessOk() {
		processor.process(new Action("ok", "data", TEST_USER_ID, Mockito.mock(Update.class)));
		Mockito.verify(userService, Mockito.times(1)).updateLastAction(anyLong(), anyString());
	}
}
