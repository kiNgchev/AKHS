package net.kingchev.telegram.component

import net.kingchev.core.kafka.CROSSPOSTING
import net.kingchev.core.model.Attachment
import net.kingchev.core.model.ContentType
import net.kingchev.core.model.CrosspostingMessage
import net.kingchev.telegram.config.TelegramProperties
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.objects.File
import org.telegram.telegrambots.meta.api.objects.Update

class ExtTelegramClient(
    private val props: TelegramProperties,
    telegramApi: TelegramBotsApi,
    private val kafkaTemplate: KafkaTemplate<String, CrosspostingMessage>
) : TelegramLongPollingBot(DefaultBotOptions(), props.token) {
    init {
        telegramApi.registerBot(this)
        logger.info("Telegram client $botUsername has been started!")
    }

    override fun getBotUsername(): String {
        return props.username ?: throw NullPointerException("Username is mustn't be null!")
    }

    override fun onUpdateReceived(update: Update) {
        if (update.channelPost == null) return
        val messageId = update.channelPost?.messageId.toString()

        val attachments = mutableListOf<Attachment>()

        if (update.channelPost.hasPhoto()) {
            val attachment = processAttachment(update.channelPost.photo.last().fileId, ContentType.IMAGE)
            attachments.add(attachment)
        }

        if (update.channelPost.hasAudio()) {
            val attachment = processAttachment(update.channelPost.audio.fileId, ContentType.AUDIO)
            attachments.add(attachment)
        }

        if (update.channelPost.hasVideo()) {
            val attachment = processAttachment(update.channelPost.video.fileId, ContentType.VIDEO)
            attachments.add(attachment)
        }

        if (update.channelPost.hasVoice()) {
            val attachment = processAttachment(update.channelPost.voice.fileId, ContentType.VOICE, "duration" to update.channelPost.voice.duration)
            attachments.add(attachment)
        }

        if (update.channelPost.hasDocument()) {
            val attachment = processAttachment(update.channelPost.document.fileId, ContentType.DOCUMENT)
            attachments.add(attachment)
        }

        val post = CrosspostingMessage(
            messageId,
            "telegram",
            "From Telegram",
            update.channelPost?.text ?: update.channelPost?.caption ?: "",
            attachments
        )

        kafkaTemplate.send(CROSSPOSTING, "telegram-message", post)
            .thenAccept { logger.info("Message with id $messageId was be produced") }
    }

    override fun onUpdatesReceived(updates: MutableList<Update>) {
        if (updates.size == 1) {
            onUpdateReceived(updates.first())
            return
        }

        val mediaGroupId = updates.first().channelPost.mediaGroupId

        val medias = mutableListOf<Attachment>()
        updates.parallelStream()
            .forEach {
                val post = it.channelPost

                if (post.hasPhoto()) medias.add(processAttachment(it.channelPost.photo.last().fileId, ContentType.IMAGE))

                if (post.hasAudio()) medias.add(processAttachment(it.channelPost.audio.fileId, ContentType.AUDIO))

                if (post.hasVideo()) medias.add(processAttachment(it.channelPost.video.fileId, ContentType.VIDEO))

                if (post.hasVoice()) medias.add(processAttachment(it.channelPost.voice.fileId, ContentType.VOICE, "duration" to it.channelPost.voice.duration))

                if (post.hasDocument()) medias.add(processAttachment(post.document.fileId, ContentType.DOCUMENT))

            }

        val post = CrosspostingMessage(
            updates.first().channelPost.messageId.toString(),
            "telegram",
            "From Telegram",
            updates.first().channelPost?.text ?: updates.first().channelPost?.caption ?: "",
            medias
        )
        kafkaTemplate.send(CROSSPOSTING, "telegram-message", post)
            .thenAccept { logger.info("Message with id $mediaGroupId was be produced") }
    }

    private fun getFile(fileId: String): File {
        return execute(GetFile(fileId))
    }

    private fun processAttachment(fileId: String, type: ContentType, vararg objects: Pair<String, Any>): Attachment {
        val fileName = getFile(fileId).getFileUrl(this.props.token).split("/").last()
        val attachment = downloadFile(getFile(fileId), java.io.File("/temp/${fileName}"))
        return Attachment(attachment.name, type, attachment.readBytes(), hashMapOf(*objects))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ExtTelegramClient::class.java)
    }
}