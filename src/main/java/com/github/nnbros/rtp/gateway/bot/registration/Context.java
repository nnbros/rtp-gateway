package com.github.nnbros.rtp.gateway.bot.registration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class Context {
	private String lastAction;
	private Instant createTime;
}
