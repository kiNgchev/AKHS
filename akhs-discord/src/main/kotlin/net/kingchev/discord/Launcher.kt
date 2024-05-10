package net.kingchev.discord

import net.kingchev.discord.config.DiscordConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(
    DiscordConfiguration::class
)
@SpringBootApplication
class AkhsDiscordApplication

object Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<AkhsDiscordApplication>(*args)
    }
}