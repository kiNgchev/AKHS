package net.kingchev.discord.kafka

import com.google.gson.Gson
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.utils.FileUpload
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.kingchev.core.kafka.CROSS_POSTING_TOPIC
import net.kingchev.core.model.CrossPostingMessage
import net.kingchev.discord.config.DiscordProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class DiscordKafkaListener(
    private val jda: JDA,
    private val gson: Gson,
    private val properties: DiscordProperties,
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

        if (message.source == "discord") return

        val channel = jda.getTextChannelById(
            properties.newsChannel ?: throw NullPointerException("News channel must not be null!")
        )

        val postBuilder = MessageCreateBuilder()

        if ((message.content?.length ?: 0) > 1960) {
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
            logger.info("Message with id ${message.id} sent")
            acknowledgment.acknowledge()
        }
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(DiscordKafkaListener::class.java)
    }
}