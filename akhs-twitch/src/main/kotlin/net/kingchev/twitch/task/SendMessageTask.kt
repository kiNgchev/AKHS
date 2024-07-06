package net.kingchev.twitch.task

import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.ITwitchClient
import com.github.twitch4j.events.ChannelGoLiveEvent
import org.springframework.stereotype.Component

@Component
class SendMessageTask(
    private val twitchClient: ITwitchClient,
    eventHandler: SimpleEventHandler,
) {
    init {
        eventHandler.onEvent(ChannelGoLiveEvent::class.java, ::onGoLive)
    }

    private fun onGoLive(event: ChannelGoLiveEvent) {
        while (true) {
            twitchClient.chat.sendMessage(event.channel.name, "Не забывайте подписываться на наш телеграм канал! https://t.me/k1ngchev")
            Thread.sleep(30 * 60 * 1000)
        }
    }
}