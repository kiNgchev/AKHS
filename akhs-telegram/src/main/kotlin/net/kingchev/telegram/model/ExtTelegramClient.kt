package net.kingchev.telegram.model

import com.google.gson.Gson
import net.kingchev.core.kafka.CROSS_POSTING_TOPIC
import net.kingchev.core.model.CrossPostingMessage
import net.kingchev.telegram.config.TelegramProperties
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.objects.Update
import java.lang.NullPointerException

class ExtTelegramClient(
    private val props: TelegramProperties,
    private val gson: Gson,
    private val telegramApi: TelegramBotsApi,
    private val kafkaTemplate: KafkaTemplate<String, String>
) : TelegramLongPollingBot(DefaultBotOptions(), props.token) {
    init {
        telegramApi.registerBot(this)
        log.info("Bot has been started!")
    }

    override fun getBotUsername(): String {
        return props.username ?: throw NullPointerException("Username is mustn't be null!")
    }

    override fun onUpdateReceived(update: Update) {
        println(update.channelPost.chatId)
        if (update.channelPost == null) return
        val post = CrossPostingMessage(
            update.channelPost.messageId.toString(),
            "telegram",
            "From Telegram",
            update.channelPost.text
        )
        val message = gson.toJson(post)
        kafkaTemplate.send(CROSS_POSTING_TOPIC, "telegram-message", message)
    }

    companion object {
        private val log = LoggerFactory.getLogger(ExtTelegramClient::class.java)
    }
}