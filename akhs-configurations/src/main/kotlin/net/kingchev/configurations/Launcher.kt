package net.kingchev.configurations

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
@EnableConfigServer
@SpringBootApplication
class AkhsConfigurationsApplication {
    //@GetMapping
    //fun index(): RedirectView = RedirectView("/main/application-default.yaml")
}

object Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<AkhsConfigurationsApplication>(*args)
    }
}