package net.kingchev.telegram.config

import com.google.gson.Gson
import net.kingchev.core.config.CommonConfiguration
import net.kingchev.telegram.model.ExtTelegramClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.KafkaTemplate
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Import(
    CommonConfiguration::class
)
@Configuration
class TelegramConfiguration(
    private val gson: Gson,
    private val props: TelegramProperties,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    @Bean
    fun telegramClient(): ExtTelegramClient {
        return ExtTelegramClient(props, gson, telegramBotApi(), kafkaTemplate)
    }

    @Bean
    fun telegramBotApi(): TelegramBotsApi {
        return TelegramBotsApi(DefaultBotSession::class.java)
    }
}