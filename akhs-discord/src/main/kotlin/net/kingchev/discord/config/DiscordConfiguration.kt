package net.kingchev.discord.config

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent.getIntents
import net.dv8tion.jda.api.requests.GatewayIntent.ALL_INTENTS
import net.dv8tion.jda.api.utils.MemberCachePolicy.ALL
import net.kingchev.core.config.CommonConfiguration
import net.kingchev.discord.event.service.EventHolderService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(
    CommonConfiguration::class
)
@Configuration
class DiscordConfiguration(
    private val properties: DiscordProperties,
    private val eventHolder: EventHolderService
) {
    @Bean
    fun jda(): JDA {
        val jda = JDABuilder.createDefault(properties.token)
            .setEnabledIntents(getIntents(ALL_INTENTS))
            .setMemberCachePolicy(ALL)
            .addEventListeners(*eventHolder.getEvents())
            .build()

        return jda
    }
}