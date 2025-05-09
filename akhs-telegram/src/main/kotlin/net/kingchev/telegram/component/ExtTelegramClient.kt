package net.kingchev.telegram.component

import net.kingchev.core.kafka.CROSSPOSTING
import net.kingchev.core.model.Attachment
import net.kingchev.core.model.CrosspostingMessage
import net.kingchev.telegram.component.processor.TelegramUpdateProcessor
import net.kingchev.telegram.config.TelegramProperties
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.telegram.telegrambots.longpolling.BotSession
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.TelegramClient


@Component
class ExtTelegramClient(
    private val props: TelegramProperties,
    private val telegramApi: TelegramClient,
    private val kafkaTemplate: KafkaTemplate<String, CrosspostingMessage>
) : LongPollingSingleThreadUpdateConsumer, SpringLongPollingBot {
    private val token = props.token!!

    init {
        logger.info("Telegram client ${props.username} has been started!")
    }

    override fun consume(update: Update) = singleUpdate(update)

    override fun consume(updates: MutableList<Update>) {
        if (updates.size == 1)
            return singleUpdate(updates[0])

        val mediaGroupId = updates.first().channelPost.mediaGroupId
        val medias = updates.stream()
            .map {
                val processor = TelegramUpdateProcessor.getProcessor(it)
                processor.processAttachment(telegramApi, token, it)
            }
            .toList()

        val post = CrosspostingMessage(
            updates.first().channelPost.messageId.toString(),
            "telegram",
            "From Telegram",
            updates.first().channelPost?.text ?: updates.first().channelPost?.caption ?: "",
            medias
        )
        kafkaTemplate.send(CROSSPOSTING, "telegram-message", post)
            .thenAccept { logger.info("Message with id $mediaGroupId was be produced") }
    }

    private fun singleUpdate(update: Update) {
        if (update.channelPost == null)
            return

        val messageId = update.channelPost?.messageId.toString()
        val attachments = mutableListOf<Attachment>()
        val processor = TelegramUpdateProcessor.getProcessor(update)
        val attachment = processor.processAttachment(telegramApi, token, update)
        attachments.add(attachment)

        val post = CrosspostingMessage(
            messageId,
            "telegram",
            "From Telegram",
            update.channelPost?.text ?: update.channelPost?.caption ?: "",
            attachments
        )

        kafkaTemplate.send(CROSSPOSTING, "telegram-message", post)
            .thenAccept { logger.info("Message with id $messageId was be produced") }
    }

    override fun getBotToken(): String? = props.token

    override fun getUpdatesConsumer(): LongPollingUpdateConsumer? = this

    @AfterBotRegistration
    fun afterRegistration(botSession: BotSession) {
        logger.info("Registered bot running state is: " + botSession.isRunning)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ExtTelegramClient::class.java)
    }
}