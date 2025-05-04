package net.kingchev.configurations

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@EnableConfigServer
@EnableAutoConfiguration
class AkhsConfigurationsApplication

object Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<AkhsConfigurationsApplication>(*args)
    }
}