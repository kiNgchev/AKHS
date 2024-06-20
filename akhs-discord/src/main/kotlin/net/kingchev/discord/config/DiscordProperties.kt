package net.kingchev.discord.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "akhs.application")
class DiscordProperties {
    var token: String? = null
    var newsChannel: String? = null
    var logsChannel: String? = null
    var color: Int = 0x7567ff
}