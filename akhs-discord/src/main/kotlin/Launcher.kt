package net.kingchev

import net.kingchev.core.config.CommonConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(
    CommonConfiguration::class
)
@SpringBootApplication
class AkhsDiscordApplication

object Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<AkhsDiscordApplication>(*args)
    }
}