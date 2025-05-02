package net.kingchev.telegram

import net.kingchev.core.config.CommonConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@Import(
    CommonConfiguration::class
)
@SpringBootApplication
class AkhsTelegramApplication

object Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<AkhsTelegramApplication>(*args)
    }
}

@RestController
class RestContr {
    @Value("akhs.application.token")
    lateinit var message: String

    @GetMapping("/message")
    fun getMessage2(): String {
        return this.message
    }
}