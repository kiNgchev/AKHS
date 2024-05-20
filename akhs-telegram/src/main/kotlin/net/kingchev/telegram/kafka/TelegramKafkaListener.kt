package net.kingchev.telegram.kafka

import com.google.gson.Gson
import net.kingchev.core.kafka.CROSS_POSTING_TOPIC
import net.kingchev.core.model.CrossPostingMessage
import net.kingchev.telegram.config.TelegramProperties
import net.kingchev.telegram.model.ExtTelegramClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Service
class TelegramKafkaListener(
    private val telegramClient: ExtTelegramClient,
    private val gson: Gson,
    private val properties: TelegramProperties,
) {
    @KafkaListener(
        topics = [CROSS_POSTING_TOPIC]
    )
    fun handleCrossPosting(
        @Payload data: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        acknowledgment: Acknowledgment
    ) {
        val message = gson.fromJson(data, CrossPostingMessage::class.java)

        if (message.source == "telegram") return

        val post = SendMessage()
        post.chatId = properties.newsChannel.toString()
        post.text = message.title + "\n\n" + message.content

        telegramClient.execute(post)
        acknowledgment.acknowledge()
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(TelegramKafkaListener::class.java)
    }
}