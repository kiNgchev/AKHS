package net.kingchev.discord.kafka

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.utils.FileUpload
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.kingchev.core.kafka.CROSSPOSTING
import net.kingchev.core.kafka.TWITCH_NOTIFICATION
import net.kingchev.core.model.CrosspostingMessage
import net.kingchev.core.model.NotificationMessage
import net.kingchev.discord.config.DiscordProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.util.UUID
import java.util.stream.Collectors
import kotlin.toString

@Service
class DiscordKafkaListener(
    private val jda: JDA,
    private val properties: DiscordProperties,
) {
    @KafkaListener(topics = [CROSSPOSTING], id = "discord-listener-crossposting")
    fun handleCrossposting(
        @Payload message: CrosspostingMessage,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        acknowledgment: Acknowledgment
    ) {
        if (message.source == "discord") return

        val channel = jda.getTextChannelById(
            properties.newsChannel ?: throw NullPointerException("News channel must not be null!")
        )

        val postBuilder = MessageCreateBuilder()

        val attachments = message.attachments.parallelStream()
            .map { FileUpload.fromData(ByteArrayInputStream(it.bytes), it.fileName) }
            .collect(Collectors.toUnmodifiableList())

        postBuilder.addFiles(attachments)

        if (message.content.length > 1960) {
            val embed = EmbedBuilder()
                .setTitle(message.title)
                .setDescription(message.content)
                .setColor(properties.color)
                .build()
            postBuilder.addEmbeds(embed)
        } else {
            postBuilder.setContent(message.title + "\n\n" + message.content)
        }

        channel?.sendMessage(postBuilder.build())?.queue {
            logger.info("Message with id ${message.id} was be consumed")
            acknowledgment.acknowledge()
        }
    }

    @KafkaListener(topics = [TWITCH_NOTIFICATION], id = "discord-listener-twitch")
    fun handleTwitchNotification(
        @Payload message: NotificationMessage,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        acknowledgment: Acknowledgment
    ) {
        val channel = jda.getTextChannelById(
            properties.newsChannel ?: throw NullPointerException("News channel must not be null!")
        )

        val notification = MessageCreateBuilder()
            .setContent(message.title + "\n\n" + message.text)
            .build()
        channel?.sendMessage(notification)?.queue {
            logger.info("Notification with id ${message.id} was be consumed")
            acknowledgment.acknowledge()
        }
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(DiscordKafkaListener::class.java)
    }
}