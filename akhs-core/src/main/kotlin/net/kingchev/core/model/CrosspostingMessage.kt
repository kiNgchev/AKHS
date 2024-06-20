package net.kingchev.core.model

import java.io.Serial
import java.io.Serializable

data class CrosspostingMessage(
    var id: String,
    var source: String,
    var title: String,
    var content: String = "",
    var attachments: List<Attachment>,
) : Serializable {
    @Serial
    private val serialVersionUID = 1L
}
