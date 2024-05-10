package net.kingchev.discord.components.listeners

import com.google.gson.Gson
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kingchev.core.kafka.CROSS_POSTING_TOPIC
import net.kingchev.core.model.CrossPostingMessage
import net.kingchev.discord.config.DiscordProperties
import net.kingchev.discord.event.annotation.AkhsListener
import net.kingchev.discord.event.model.Event
import org.springframework.kafka.core.KafkaTemplate

@AkhsListener
class CrossPostingListener(
    private val gson: Gson,
    private val properties: DiscordProperties,
    private val kafkaTemplate: KafkaTemplate<String, String>
) : ListenerAdapter(), Event {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.channel.id != properties.newsChannel || event.author.isBot) return
        val post = CrossPostingMessage(
            event.messageId,
            "discord",
            "From Discord",
            event.message.contentRaw
        )
        val message = gson.toJson(post)
        kafkaTemplate.send(CROSS_POSTING_TOPIC, "discord-message", message)
    }
}