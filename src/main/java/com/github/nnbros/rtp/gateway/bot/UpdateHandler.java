package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Function;

import static com.github.nnbros.rtp.gateway.util.BotUtils.getUserId;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateHandler implements Function<Update, BotApiMethod<?>> {
	private static final String CHAT_ID_MDC_KEY = "chatId";
	private static final String UPDATE_ID_MDC_KEY = "updateId";
	public static final String UNKNOWN_UPDATE_RESPONSE_MESSAGE = "Sorry, there is no functionality implemented to process your request.";

	private final CommandService commandService;
	private final MessageBuilder messageBuilder;

	@Override
	public BotApiMethod<?> apply(Update update) {
		Integer updateId = update.getUpdateId();
		try {
			log.info("New update has been received, update id is {}", updateId);
			log.trace("Update:\n{}", update);

			UpdateType updateType = UpdateType.getUpdateType(update);
			log.debug("Update type is [{}]", updateType);
			String userId = getUserId(update, updateType);
			MDC.put(CHAT_ID_MDC_KEY, "[%s]".formatted(userId));
			MDC.put(UPDATE_ID_MDC_KEY, "[%d]".formatted(updateId));

			BotApiMethod<?> response;
			if (updateType == UpdateType.COMMAND) {
				response = commandService.processUpdate(update);
			} else {
				log.info("The update type is unknown and it will not be processed");
				response = messageBuilder.createMessage(userId, UNKNOWN_UPDATE_RESPONSE_MESSAGE);
			}

			log.debug("Response type for the update: {}", response.getMethod());
			log.info("The update has been processed successfully");
			return response;
		} catch (Exception e) {
			//TODO add retry logic and updates validation
			throw new GatewayRuntimeException("Failed to process update %s".formatted(updateId), e);
		} finally {
			MDC.clear();
		}
	}
}
