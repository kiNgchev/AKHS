package net.kingchev.admins

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableAdminServer
@SpringBootApplication
class AkhsAdminsApplication

fun main(args: Array<String>) {
    runApplication<AkhsAdminsApplication>(*args)
}