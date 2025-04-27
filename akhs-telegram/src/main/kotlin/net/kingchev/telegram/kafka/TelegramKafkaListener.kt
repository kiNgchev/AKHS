package net.kingchev.telegram.kafka

import net.kingchev.core.kafka.CROSSPOSTING
import net.kingchev.core.kafka.TWITCH_NOTIFICATION
import net.kingchev.core.model.*
import net.kingchev.telegram.component.ExtTelegramClient
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
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.media.*
import java.io.ByteArrayInputStream
import java.io.InputStream

@Service
class TelegramKafkaListener(
    private val telegramClient: ExtTelegramClient,
    private val properties: TelegramProperties,
) {
    @KafkaListener(topics = [CROSSPOSTING], id = "telegram-listener-crossposting")
    fun handleCrossposting(
        @Payload message: CrosspostingMessage,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        acknowledgment: Acknowledgment
    ) {
        if (message.source == "telegram") return

        if (properties.newsChannel == null) throw NullPointerException("News channel must not be null!")
        val chatId = properties.newsChannel.toString()
        val text = message.title + "\n\n" + message.content

        val postMedia = mutableListOf<InputMedia>()

        message.attachments.forEach {
            if (it.isVoice()) sendVoice(chatId, text, it)

            if (message.attachments.size == 1) {
                when (it.contentType) {
                    ContentType.IMAGE -> telegramClient.execute(processMedia<SendPhoto>(chatId, text, "photo", it))
                    ContentType.VIDEO -> telegramClient.execute(processMedia<SendVideo>(chatId, text, "video", it))
                    ContentType.AUDIO -> telegramClient.execute(processMedia<SendAudio>(chatId, text, "audio", it))
                    ContentType.DOCUMENT -> telegramClient.execute(processMedia<SendDocument>(chatId, text, "document", it))
                    else -> {}
                }
                return@forEach
            }

            when (it.contentType) {
                ContentType.IMAGE -> postMedia.add(processMedia<InputMediaPhoto>(it))
                ContentType.VIDEO -> postMedia.add(processMedia<InputMediaVideo>(it))
                ContentType.AUDIO -> postMedia.add(processMedia<InputMediaAudio>(it))
                ContentType.DOCUMENT -> postMedia.add(processMedia<InputMediaDocument>(it))
                else -> {}
            }
        }

        if (postMedia.size in 2..10) {
            postMedia.last().caption = text
            val post = SendMediaGroup(chatId, postMedia)
            telegramClient.execute(post)
        }

        if (message.attachments.isEmpty()) {
            val post = SendMessage(chatId, message.title + "\n\n" + message.content)
            telegramClient.execute(post)
        }

        logger.info("Message with id ${message.id} was be consumed")
        acknowledgment.acknowledge()
    }

    @KafkaListener(topics = [TWITCH_NOTIFICATION], id = "telegram-listener-twitch")
    fun handleTwitchNotification(
        @Payload message: NotificationMessage,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        acknowledgment: Acknowledgment
    ) {
        val notification = SendMessage()
        notification.chatId = properties.newsChannel.toString()
        notification.text = message.title + "\n\n" + message.text

        telegramClient.execute(notification)

        logger.info("Notification with id ${message.id} was be consumed")
        acknowledgment.acknowledge()
    }

    private fun sendVoice(chatId: String, caption: String, attachment: Attachment) {
        telegramClient.execute(processMedia<SendVoice>(chatId, caption, "voice", attachment))
    }

    private inline fun <reified T : InputMedia> processMedia(attachment: Attachment): T {
        val input = T::class.java.getDeclaredConstructor().newInstance()

        val setMediaMethod =
            T::class.java.superclass.getDeclaredMethod("setMedia", InputStream::class.java, String::class.java)
        setMediaMethod.invoke(input, ByteArrayInputStream(attachment.bytes), attachment.fileName)

        return input
    }

    private inline fun <reified T : SendMediaBotMethod<Message>> processMedia(
        chatId: String,
        caption: String,
        field: String,
        attachment: Attachment
    ): T {
        val method = T::class.java.getDeclaredConstructor().newInstance()

        val chatIdField = method::class.java.getDeclaredField("chatId")
        chatIdField.isAccessible = true
        chatIdField.set(method, chatId)

        val captionField = method::class.java.getDeclaredField("caption")
        captionField.isAccessible = true
        captionField.set(method, caption)

        val mediaField = method::class.java.getDeclaredField(field)
        mediaField.isAccessible = true
        mediaField.set(method, InputFile(ByteArrayInputStream(attachment.bytes), attachment.fileName))
        return method
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(TelegramKafkaListener::class.java)
    }
}