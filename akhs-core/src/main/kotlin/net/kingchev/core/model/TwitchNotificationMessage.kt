package net.kingchev.core.model

import java.io.Serial
import java.io.Serializable

data class TwitchNotificationMessage(
    var id: String,
    var title: String,
    var text: String,
    var url: String? = null,
    var type: TwitchNotificationType
) : Serializable {
    @Serial
    private val serialVersionUID = 2L
}
