package org.flowninja.common.akka;

import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActorUtils {
	public static MessageHandler withMessage(final Object message) {
		log.info("Handling message of type {}", message.getClass().getName());

		return new MessageHandler(message);
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static class MessageHandler {
		private final Object message;

		@SuppressWarnings("unchecked")
		public <T> MessageHandler onType(final Class<T> messageType, final Consumer<T> messageHandler)
				throws Throwable {
			log.info("Checking match for type {} on message {}", messageType.getName(),
					message != null ? message.getClass().getName() : "null");

			if (message != null && messageType.isInstance(message)) {
				log.info("Match for type {} on message {}", messageType.getName(), message.getClass().getName());

				messageHandler.accept((T) message);

				return new MessageHandler(null);
			}

			return new MessageHandler(message);
		}

		public void unhandled(final Consumer<Object> defaultHandler) {
			if (message != null) {
				log.info("Unhandled message {}", message.getClass().getName());

				defaultHandler.accept(message);
			}
		}
	}
}
