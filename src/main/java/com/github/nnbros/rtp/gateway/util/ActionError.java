package com.github.nnbros.rtp.gateway.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ActionError {
	ERROR_USER_ALREADY_REGISTERED("Вы уже зарегистрированы."),
	ERROR_USER_NOT_REGISTERED("Команда не может быть обработана. Необходимо пройти регистрацию"),
	ERROR_HELP("Здесь когда-нибудь будет справка по боту."),
	ERROR_NOT_ALLOWED("Команда не может быть обработана. Попробуйте выполнить другое действие"),
	ERROR_UNKNOWN("Не знаю такой команды :(");

	private final String text;

	public static Set<String> names() {
		return Arrays.stream(ActionError.values())
				.map(Enum::name)
				.collect(Collectors.toSet());
	}
}
