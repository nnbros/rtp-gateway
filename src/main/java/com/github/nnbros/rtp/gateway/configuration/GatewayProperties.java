package com.github.nnbros.rtp.gateway.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {
	@NotNull
	private RTPBot rtpBot = new RTPBot();
	@NotBlank
	private String certificateName = "rtpbot-chain.crt";
	@NotBlank
	private String certificatePath = "certificates/rtpbot-chain.crt";

	@Setter
	@Getter
	public static class RTPBot {
		private boolean webhookEnabled = true;
		@NotNull
		private URI url = URI.create("https://rtp-bot.ru:8443");
		@NotBlank
		private String webhookPath = "rtp";
		@Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
		private String ip;
		@NotBlank
		private String token;
	}
}
