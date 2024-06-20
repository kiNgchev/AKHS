package net.kingchev.telegram.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "akhs.application")
class TelegramProperties {
    var token: String? = null
    var newsChannel: String? = null
    var username: String? = null
    var logsChannel: String? = null
}