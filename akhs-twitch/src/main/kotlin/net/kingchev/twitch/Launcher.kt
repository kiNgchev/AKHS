package net.kingchev.twitch

import net.kingchev.core.config.CommonConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(
    CommonConfiguration::class
)
@SpringBootApplication
class AkhsTwitchApplication

object Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<AkhsTwitchApplication>(*args)
    }
}