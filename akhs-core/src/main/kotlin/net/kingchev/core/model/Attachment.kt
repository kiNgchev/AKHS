package net.kingchev.core.model

data class Attachment(
    var fileName: String,
    var contentType: ContentType,
    var attachment: List<Byte>
)
