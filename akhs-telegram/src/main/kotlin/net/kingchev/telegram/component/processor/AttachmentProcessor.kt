package net.kingchev.telegram.component.processor

import net.kingchev.core.model.Attachment
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.send.SendVideo
import org.telegram.telegrambots.meta.api.methods.send.SendVoice
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.media.InputMedia
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAudio
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo
import org.telegram.telegrambots.meta.generics.TelegramClient
import java.io.ByteArrayInputStream
import javax.naming.OperationNotSupportedException

interface AttachmentProcessor {
    fun processSingle(telegramApi: TelegramClient, attachment: Attachment, chatId: String, text: String)
    
    fun processList(telegramApi: TelegramClient, attachment: Attachment, chatId: String, text: String): InputMedia
    
    companion object {
        private val processors = mapOf(
            "image" to ImageProcessor,
            "video" to VideoProcessor,
            "audio" to AudioProcessor,
            "document" to DocumentProcessor,
            "voice" to VoiceProcessor
        )

        fun getProcessor(type: String): AttachmentProcessor =
            processors[type] ?: throw IllegalArgumentException("Unknown attachment type: $type")
    }
}

object ImageProcessor : AttachmentProcessor {
    override fun processSingle(telegramApi: TelegramClient, attachment: Attachment, chatId: String, text: String) {
        val photo = SendPhoto.builder()
            .chatId(chatId)
            .caption(text)
            .photo(attachment.toFile())
            .build()
        telegramApi.execute(photo)
        return
    }

    override fun processList(telegramApi: TelegramClient, attachment: Attachment, chatId: String, text: String): InputMedia =
        InputMediaPhoto.builder()
            .caption(text)
            .media(ByteArrayInputStream(attachment.bytes), attachment.fileName)
            .build()
}

object VideoProcessor : AttachmentProcessor {
    override fun processSingle(telegramApi: TelegramClient, attachment: Attachment,  chatId: String, text: String) {
        val video = SendVideo.builder()
            .chatId(chatId)
            .caption(text)
            .video(attachment.toFile())
            .build()
        telegramApi.execute(video)
        return
    }

    override fun processList(telegramApi: TelegramClient, attachment: Attachment, chatId: String, text: String): InputMedia =
        InputMediaVideo.builder()
            .caption(text)
            .media(ByteArrayInputStream(attachment.bytes), attachment.fileName)
            .build()
}

object AudioProcessor : AttachmentProcessor {
    override fun processSingle(telegramApi: TelegramClient, attachment: Attachment,  chatId: String, text: String) {
        val audio = SendAudio.builder()
            .chatId(chatId)
            .caption(text)
            .audio(attachment.toFile())
            .build()
        telegramApi.execute(audio)
        return
    }

    override fun processList(telegramApi: TelegramClient, attachment: Attachment, chatId: String, text: String): InputMedia =
        InputMediaAudio.builder()
            .caption(text)
            .media(ByteArrayInputStream(attachment.bytes), attachment.fileName)
            .build()
}

object DocumentProcessor : AttachmentProcessor {
    override fun processSingle(telegramApi: TelegramClient, attachment: Attachment,  chatId: String, text: String) {
        val document = SendDocument.builder()
            .chatId(chatId)
            .caption(text)
            .document(attachment.toFile())
            .build()
        telegramApi.execute(document)
        return
    }

    override fun processList(telegramApi: TelegramClient, attachment: Attachment, chatId: String, text: String): InputMedia =
        InputMediaDocument.builder()
            .caption(text)
            .media(ByteArrayInputStream(attachment.bytes), attachment.fileName)
            .build()
}

object VoiceProcessor : AttachmentProcessor {
    override fun processSingle(telegramApi: TelegramClient, attachment: Attachment,  chatId: String, text: String) {
        val voice = SendVoice.builder()
            .chatId(chatId)
            .caption(text)
            .voice(attachment.toFile())
            .build()
        telegramApi.execute(voice)
        return
    }

    override fun processList(telegramApi: TelegramClient, attachment: Attachment, chatId: String, text: String): InputMedia =
        throw OperationNotSupportedException("YOU CAN NOT PROCESS LIST OF VOICE MESSAGES")
}

private fun Attachment.toFile(): InputFile =
    InputFile(ByteArrayInputStream(this.bytes), this.fileName)