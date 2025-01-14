package com.github.nnbros.rtp.gateway.bot.update.processor;

import com.github.nnbros.rtp.gateway.bot.update.Action;
import com.github.nnbros.rtp.gateway.client.StorytellerServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nnbros.rtp.gateway.bot.BotTestUtils.TEST_USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class StorytellerActionProcessorTest {
	@Mock
	private StorytellerServiceClient storytellerServiceClient;
	@InjectMocks
	private StorytellerActionProcessor processor;

	@Test
	void checkProcessOk() {
		processor.process(new Action("ok", "data", TEST_USER_ID, Mockito.mock(Update.class)));
		Mockito.verify(storytellerServiceClient, Mockito.times(1)).sendAction(anyString(), anyString(), any(Update.class));
	}
}
