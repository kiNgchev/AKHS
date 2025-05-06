package net.kingchev.configurations

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@EnableConfigServer
@SpringBootApplication
class AkhsConfigurationsApplication

fun main(args: Array<String>) {
    runApplication<AkhsConfigurationsApplication>(*args)
}
