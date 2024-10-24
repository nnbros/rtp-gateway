package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.configuration.GatewayProperties;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WebhookSetterTest {
	@MockBean
	private TelegramClient telegramClient;

	@Autowired
	private GatewayProperties properties;

	private WebhookSetter webhookSetter;

	@BeforeEach
	void setUp() {
		webhookSetter = new WebhookSetter(telegramClient, properties);
		clearInvocations(telegramClient);
	}

	@Test
	void setWebhook() throws Exception {
		try (InputStream certInputStream = WebhookSetter.class.getResourceAsStream("/certificates/test.crt")) {
			assertNotNull(certInputStream);

			webhookSetter.run();

			ArgumentCaptor<SetWebhook> captor = ArgumentCaptor.captor();
			verify(telegramClient, times(1)).execute(captor.capture());

			SetWebhook webhook = captor.getValue();
			InputFile certificate = webhook.getCertificate();
			InputStream actualCertStream = certificate.getNewMediaStream();
			assertArrayEquals(certInputStream.readAllBytes(), actualCertStream.readAllBytes());
			actualCertStream.close();

			assertEquals("test.crt", certificate.getMediaName());
			assertEquals("https://test.com:1234/testPath", webhook.getUrl());
			assertEquals("127.0.0.1", webhook.getIpAddress());
		}
	}

	@Test
	void throwExceptionIfCertificateNotFound() {
		String oldCertificatePath = properties.getCertificatePath();
		try {
			properties.setCertificatePath("wrongPath");

			assertThrows(GatewayRuntimeException.class, webhookSetter::run);
			verifyNoInteractions(telegramClient);
		} finally {
			properties.setCertificatePath(oldCertificatePath);
		}
	}

	@Test
	void throwExceptionOnTelegramApiException() throws TelegramApiException {
		doThrow(new TelegramApiException("Telegram API error")).when(telegramClient).execute(any(SetWebhook.class));

		assertThrows(GatewayRuntimeException.class, webhookSetter::run);
		verify(telegramClient, times(1)).execute(any(SetWebhook.class));
	}
}
