package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.configuration.GatewayProperties;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;

import static com.github.nnbros.rtp.gateway.util.ResourceUtils.getResourceAsStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookSetter implements Runnable {
	private final TelegramClient telegramClient;
	private final GatewayProperties properties;

	@Override
	public void run() {
		try {
			log.info("Setting the bot webhook...");
			GatewayProperties.RTPBot rtpBotProperties = properties.getRtpBot();
			String certificatePath = properties.getCertificatePath();
			log.debug("Getting certificate");
			InputStream certInputStream = getResourceAsStream(certificatePath)
					.orElseThrow(() -> new GatewayRuntimeException("Failed to get certificate with path: %s".formatted(certificatePath)));

			InputFile cert = new InputFile();
			cert.setMedia(certInputStream, properties.getCertificateName());
			log.debug("Creating set webhook request");
			SetWebhook webhook = SetWebhook.builder()
					.url("%s/%s".formatted(rtpBotProperties.getUrl(), rtpBotProperties.getWebhookPath()))
					.ipAddress(rtpBotProperties.getIp())
					.certificate(cert)
					.build();

			log.debug("Sending webhook request to the Telegram bot API");
			telegramClient.execute(webhook);
			log.info("Webhook has been set successfully");
		} catch (TelegramApiException e) {
			//TODO add retry logic
			throw new GatewayRuntimeException("Failed to set rtp bot webhook", e);
		}
	}
}
