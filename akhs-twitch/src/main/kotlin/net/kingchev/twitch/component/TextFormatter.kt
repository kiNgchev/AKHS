package net.kingchev.twitch.component

import com.github.twitch4j.events.ChannelGoLiveEvent

object TextFormatter {
    private val templates = listOf(
        "{channel.name} Запустил трансляцию {stream.title}\nНе пропусти! {stream.url}"
    )
    private val values = { event: ChannelGoLiveEvent -> mapOf(
        "channel.name" to event.channel.name,
        "channel.id" to event.channel.id,
        "stream.title" to event.stream.title,
        "stream.url" to "https://www.twitch.tv/${event.channel.name}"
    ) }

    fun format(event: ChannelGoLiveEvent): String {
        val template = templates.random()
        return format(template, values(event))
    }

    private fun format(template: String, map: Map<String, String>): String =
        map.entries.fold(template) { acc, entry ->
            acc.replace("{${entry.key}}", entry.value)
        }
}