package com.github.nnbros.rtp.gateway.bot.action;

import lombok.*;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Class represents an action which the user performed.
 * IMPORTANT: do not transform it into Java Record because it's used for Kryo serialization during state machine context saving
 * and Kryo cannot work with Java Record properly.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "update")
public class Action {
	private String actionId;
	private String data;
	private Long userId;
	private transient Update update;
}


