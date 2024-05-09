package net.kingchev.core.model

import java.io.File

data class Attachment(
    var id: String,
    var type: AttachmentType,
    var attachment: File
)
