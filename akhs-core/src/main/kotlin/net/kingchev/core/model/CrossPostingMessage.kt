package net.kingchev.core.model

data class CrossPostingMessage(
    var id: String,
    var source: String,
    var title: String,
    var content: String?,
)
