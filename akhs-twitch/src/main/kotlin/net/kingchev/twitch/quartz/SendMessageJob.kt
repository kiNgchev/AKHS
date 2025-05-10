package net.kingchev.twitch.quartz

import com.github.twitch4j.ITwitchClient
import net.kingchev.twitch.config.TwitchProperties
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SendMessageJob(
    private val twitchClient: ITwitchClient,
    private val props: TwitchProperties
) {
    @Scheduled(fixedDelay = 130 * 60 * 1000)
    private fun sendMessage() {
        props.twitchChannel.forEach {
            twitchClient.chat.sendMessage(
                it,
                "Не забывайте подписываться на наш телеграм канал! https://t.me/k1ngchev"
            )
        }
    }
}