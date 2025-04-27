package net.kingchev.core.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.kingchev.core.model.Attachment
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.envers.Audited
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "posts")
@Audited
data class Post(
    @Id
    val id: Long? = null,
    var source: String? = null,
    var title: String? = null,
    var content: String = "",

    @JdbcTypeCode(SqlTypes.JSON)
    var attachments: List<Attachment> = listOf(),
)