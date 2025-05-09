package net.kingchev.telegram.kafka

import net.kingchev.core.kafka.CROSSPOSTING
import net.kingchev.core.kafka.TWITCH_NOTIFICATION
import net.kingchev.core.model.*
import net.kingchev.telegram.component.processor.AttachmentProcessor
import net.kingchev.telegram.component.processor.MessageProcessor
import net.kingchev.telegram.config.TelegramProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.media.InputMedia
import org.telegram.telegrambots.meta.generics.TelegramClient

@Service
class TelegramKafkaListener(
    private val telegramClient: TelegramClient,
    private val properties: TelegramProperties,
) {
    @KafkaListener(topics = [CROSSPOSTING], id = "telegram-listener-crossposting", clientIdPrefix = "3", groupId = "telegram-listeners")
    fun handleCrossposting(
        @Payload message: CrosspostingMessage,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        acknowledgment: Acknowledgment
    ) {
        if (message.source == "telegram") return

        if (properties.newsChannel == null) throw NullPointerException("News channel must not be null!")
        val chatId = properties.newsChannel.toString()
        val text = message.title + "\n\n" + message.content
        val postMedias =  mutableListOf<InputMedia>()

        message.attachments.forEach loop@{ attachment ->
            val processor = AttachmentProcessor.getProcessor(attachment.contentType.toString())
            if (message.attachments.size == 1) {
                processor.processSingle(telegramClient, attachment, chatId, text)
                return@loop
            }

            postMedias.add(processor.processList(telegramClient, attachment, chatId, text))
        }

        val processor = MessageProcessor.getProcessor(postMedias.size)
        processor.process(telegramClient, postMedias, chatId, text)

        logger.info("Message with id ${message.id} was be consumed")
        acknowledgment.acknowledge()
    }

    @KafkaListener(topics = [TWITCH_NOTIFICATION], id = "telegram-listener-twitch", clientIdPrefix = "4", groupId = "telegram-listeners")
    fun handleTwitchNotification(
        @Payload message: NotificationMessage,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        acknowledgment: Acknowledgment
    ) {
        val notification = SendMessage
            .builder()
            .chatId(properties.newsChannel.toString())
            .text(message.title + "\n\n" + message.text)
            .build()

        telegramClient.execute(notification)

        logger.info("Notification with id ${message.id} was be consumed")
        acknowledgment.acknowledge()
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(TelegramKafkaListener::class.java)
    }
}