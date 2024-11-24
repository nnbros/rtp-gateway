package com.github.nnbros.rtp.gateway.bot;

import com.github.nnbros.rtp.gateway.bot.update.Action;
import com.github.nnbros.rtp.gateway.bot.update.LockService;
import com.github.nnbros.rtp.gateway.bot.update.UpdateType;
import com.github.nnbros.rtp.gateway.bot.update.processor.ErrorActionProcessor;
import com.github.nnbros.rtp.gateway.bot.update.provider.ActionProvider;
import com.github.nnbros.rtp.gateway.exception.GatewayRuntimeException;
import com.github.nnbros.rtp.gateway.exception.UpdateRuntimeException;
import com.github.nnbros.rtp.gateway.model.Events;
import com.github.nnbros.rtp.gateway.model.States;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.EnumMap;
import java.util.function.Function;

import static com.github.nnbros.rtp.gateway.util.BotUtils.getUserId;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateHandler implements Function<Update, BotApiMethod<?>> {
	private final EnumMap<UpdateType, ActionProvider> actionProviders;
	private final LockService lockService;
	private final MessageBuilder messageBuilder;
	private final ErrorActionProcessor errorProcessor;
	private final StateMachine<States, Events> stateMachine;
	private static final String CHAT_ID_MDC_KEY = "chatId";
	private static final String UPDATE_ID_MDC_KEY = "updateId";
	public static final String UNKNOWN_UPDATE_RESPONSE_MESSAGE = "Sorry, there is no functionality implemented to process your request.";

	@Override
	public BotApiMethod<?> apply(Update update) {
		Integer updateId = update.getUpdateId();
		Long userId = 0L;
		try {
			log.info("New update has been received, update id is {}", updateId);
			log.trace("Update:\n{}", update);

			UpdateType updateType = UpdateType.getUpdateType(update);
			log.debug("Update type is [{}]", updateType);
			userId = getUserId(update, updateType);
			MDC.put(CHAT_ID_MDC_KEY, "[%s]".formatted(userId));
			MDC.put(UPDATE_ID_MDC_KEY, "[%d]".formatted(updateId));

			ActionProvider actionProvider;
			if (actionProviders.containsKey(updateType)) {
				actionProvider = actionProviders.get(updateType);
			} else {
				log.info("The update type is unknown and it will not be processed");
				return messageBuilder.createMessage(userId, UNKNOWN_UPDATE_RESPONSE_MESSAGE);
			}
			if (lockService.isLocked(userId)) {
				log.warn("There is an action in progress for user {} , received update {} won't be processed", userId, updateId);
			} else {
				lockService.createLock(userId);

				Action action = actionProvider.retrieve(update);

				stateMachine.getExtendedState().getVariables().put("ACTION", action);
				boolean accepted = stateMachine.sendEvent(Events.valueOf(action.actionId()));

				log.info("The update has been processed successfully, accepted = {}", accepted);
				if (!accepted) {
					throw new UpdateRuntimeException("Команда не может быть обработана. Попробуйте выполнить другое действие", action.userId());
				}
			}
		} catch (UpdateRuntimeException e) {
			errorProcessor.process(userId, e.getMessage());
		} catch (Exception e) {
			//TODO add retry logic and updates validation
			lockService.releaseLock(userId);
			throw new GatewayRuntimeException("Failed to process update %s".formatted(updateId), e);
		} finally {
			MDC.clear();
		}
		return null;
	}
}
