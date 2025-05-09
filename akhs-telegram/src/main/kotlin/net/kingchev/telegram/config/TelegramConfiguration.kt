package net.kingchev.telegram.config

import net.kingchev.core.config.CommonConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.meta.generics.TelegramClient

@Import(
    CommonConfiguration::class
)
@Configuration
class TelegramConfiguration(private val props: TelegramProperties, ) {
    @Bean
    fun telegramBotApi(): TelegramClient =
        OkHttpTelegramClient(props.token)
}