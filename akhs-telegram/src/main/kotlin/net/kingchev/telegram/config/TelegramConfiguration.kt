package net.kingchev.telegram.config

import net.kingchev.core.config.CommonConfiguration
import net.kingchev.core.model.CrosspostingMessage
import net.kingchev.telegram.component.ExtTelegramClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Scope
import org.springframework.kafka.core.KafkaTemplate
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Import(
    CommonConfiguration::class
)
@Configuration
class TelegramConfiguration(
    private val props: TelegramProperties,
    private val kafkaTemplate: KafkaTemplate<String, CrosspostingMessage>
) {

    @Bean
    @Scope("singleton")
    fun telegramClient(): ExtTelegramClient {
        return ExtTelegramClient(props, telegramBotApi(), kafkaTemplate)
    }

    @Bean
    fun telegramBotApi(): TelegramBotsApi {
        return TelegramBotsApi(DefaultBotSession::class.java)
    }
}