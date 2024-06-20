package net.kingchev.twitch.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "akhs.application")
class TwitchProperties {
    var tokens = Tokens()

    var twitchChannel: List<String> = mutableListOf()

    var username: String? = null

    @ConfigurationProperties(prefix = "akhs.application.tokens")
    class Tokens {
        var clientId: String? = null
        var clientSecret: String? = null
        var irc: String? = null
    }
}

