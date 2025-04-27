package net.kingchev.core.model

import java.io.Serial
import java.io.Serializable

data class NotificationMessage(
    var id: String,
    var source: String,
    var title: String,
    var text: String,
    var url: String? = null,
    var type: NotificationType
) : Serializable {
    @Serial
    private val serialVersionUID = 2L
}
