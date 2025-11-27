package com.github.nnbros.rtp.gateway.configuration;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {
	@NotBlank
	private String certificateName = "rtpbot-chain.crt";
	@NotBlank
	private String certificatePath = "certificates/rtpbot-chain.crt";
}
