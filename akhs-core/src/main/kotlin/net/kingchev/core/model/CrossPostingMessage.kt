package net.kingchev.core.model

import java.time.OffsetDateTime

data class CrossPostingMessage(
    var id: String,
    var title: String?,
    var content: String?,
    var data: List<Attachment>,
    var timestamp: OffsetDateTime?
)
