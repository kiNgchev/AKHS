package net.kingchev.discord.components.listeners

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kingchev.core.kafka.CROSSPOSTING
import net.kingchev.core.model.Attachment
import net.kingchev.core.model.ContentType
import net.kingchev.core.model.CrosspostingMessage
import net.kingchev.discord.config.DiscordProperties
import net.kingchev.discord.event.annotation.AkhsListener
import net.kingchev.discord.event.model.Event
import org.springframework.kafka.core.KafkaTemplate

@AkhsListener
class CrosspostingListener(
    private val properties: DiscordProperties,
    private val kafkaTemplate: KafkaTemplate<String, CrosspostingMessage>
) : ListenerAdapter(), Event {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.channel.id != properties.newsChannel || event.author.isBot) return

        val attachments = mutableListOf<Attachment>()
        event.message.attachments.forEach {
            if (it.contentType?.startsWith("image/") == true) {
                attachments.add(Attachment(it.fileName, ContentType.IMAGE, it.proxy.download().get().readAllBytes()))
            } else if (it.contentType?.startsWith("video/") == true) {
                attachments.add(Attachment(it.fileName, ContentType.VIDEO, it.proxy.download().get().readAllBytes()))
            } else if (it.contentType?.startsWith("audio/") == true && it.waveform != null) {
                attachments.add(Attachment(it.fileName, ContentType.VOICE, it.proxy.download().get().readAllBytes()))
            } else if (it.contentType?.startsWith("audio/") == true && it.waveform == null) {
                attachments.add(Attachment(it.fileName, ContentType.AUDIO, it.proxy.download().get().readAllBytes()))
            } else {
                attachments.add(Attachment(it.fileName, ContentType.DOCUMENT, it.proxy.download().get().readAllBytes()))
            }
        }

        val post = CrosspostingMessage(
            event.messageId,
            "discord",
            "From Discord",
            event.message.contentRaw,
            attachments
        )

        kafkaTemplate.send(CROSSPOSTING, "discord-message", post)
    }
}