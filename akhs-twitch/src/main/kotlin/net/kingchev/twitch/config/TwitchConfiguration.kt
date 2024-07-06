package net.kingchev.twitch.config

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.ITwitchClient
import com.github.twitch4j.TwitchClientBuilder
import net.kingchev.core.config.CommonConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(
    CommonConfiguration::class
)
@Configuration
class TwitchConfiguration(
    private val properties: TwitchProperties
) {
    @Bean
    fun simpleEventHandler(twitchClient: ITwitchClient): SimpleEventHandler {
        return twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java)
    }

    @Bean
    fun twitchClient(): ITwitchClient {
        val client = TwitchClientBuilder.builder()
            .withChatAccount(OAuth2Credential("twitch", properties.tokens.irc))
            .withEnableChat(true)
            .withClientId(properties.tokens.clientId)
            .withClientSecret(properties.tokens.clientSecret)
            .withEnableHelix(true)
            .build()
        properties.twitchChannel.forEach {
            client.chat.joinChannel(it)
        }

        client.clientHelper.enableStreamEventListener(properties.twitchChannel)

        return client
    }
}