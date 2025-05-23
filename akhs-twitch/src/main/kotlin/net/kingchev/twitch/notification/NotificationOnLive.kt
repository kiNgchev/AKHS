package net.kingchev.twitch.notification

import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.events.ChannelGoLiveEvent
import net.kingchev.core.kafka.TWITCH_NOTIFICATION
import net.kingchev.core.model.NotificationMessage
import net.kingchev.core.model.NotificationType
import net.kingchev.twitch.component.TextFormatter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class NotificationOnLive(
    private val eventHandler: SimpleEventHandler,
    private val kafkaTemplate: KafkaTemplate<String, NotificationMessage>
) {
    init {
        eventHandler.onEvent(ChannelGoLiveEvent::class.java, ::onGoLive)
    }

    private fun onGoLive(event: ChannelGoLiveEvent) {
        val url = "https://www.twitch.tv/${event.channel.name}"
        val message = NotificationMessage(
            "twitch${event.stream.id}",
            "twitch",
            event.channel.name,
            TextFormatter.format(event),
            url,
            NotificationType.LIVE_NOTIFICATION
        )
        kafkaTemplate
            .send(TWITCH_NOTIFICATION, message)
            .thenAccept { logger.info("Message with id ${message.id} was be produced") }
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(NotificationOnLive::class.java)
    }
}