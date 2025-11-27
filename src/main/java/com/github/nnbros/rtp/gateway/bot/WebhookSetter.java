package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.common.autoconfigure.RtpBotProperties;
import com.github.nnbros.rtp.gateway.configuration.GatewayProperties;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rtp-bot.webhook-enabled", havingValue = "true")
public class WebhookSetter implements Runnable {
	public static final String FAILED_TO_GET_CERTIFICATE_ERROR_TEMPLATE = "Failed to get certificate with path: %s";
	private final TelegramClient telegramClient;
	private final GatewayProperties gatewayProperties;
	private final RtpBotProperties rtpBotProperties;
	private final ResourceLoader resourceLoader;

	@Override
	public void run() {
		String certificatePath = gatewayProperties.getCertificatePath();
		try {
			log.info("Setting the bot webhook...");
			log.debug("Getting certificate");
			InputStream certInputStream;
			Resource certificateResource = resourceLoader.getResource(certificatePath);
			if (certificateResource.exists()) {
				certInputStream = certificateResource.getInputStream();
			} else {
				throw new GatewayRuntimeException(FAILED_TO_GET_CERTIFICATE_ERROR_TEMPLATE, certificatePath);
			}

			InputFile cert = new InputFile();
			cert.setMedia(certInputStream, gatewayProperties.getCertificateName());
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
		} catch (IOException e) {
			throw new GatewayRuntimeException(FAILED_TO_GET_CERTIFICATE_ERROR_TEMPLATE, certificatePath);
		}
	}
}
