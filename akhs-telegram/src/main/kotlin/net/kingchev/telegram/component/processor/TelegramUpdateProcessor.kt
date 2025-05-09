package net.kingchev.telegram.component.processor

import net.kingchev.core.model.Attachment
import net.kingchev.core.model.ContentType
import net.kingchev.core.model.emptyAttachment
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.objects.File
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.TelegramClient

interface TelegramUpdateProcessor {
    fun processAttachment(telegramClient: TelegramClient, token: String, update: Update): Attachment

    companion object {
        private val processors: (Update) -> TelegramUpdateProcessor = { update: Update ->
            when {
                update.channelPost.hasPhoto() -> ImageUpdateProcessor
                update.channelPost.hasVideo() -> VideoUpdateProcessor
                update.channelPost.hasAudio() -> AudioUpdateProcessor
                update.channelPost.hasDocument() -> DocumentUpdateProcessor
                update.channelPost.hasVoice() -> VoiceUpdateProcessor
                else -> EmptyUpdateProcessor
            }
        }

        fun getProcessor(update: Update): TelegramUpdateProcessor = processors(update)
    }
}

object EmptyUpdateProcessor : TelegramUpdateProcessor {
    override fun processAttachment(telegramClient: TelegramClient, token: String, update: Update): Attachment {
        return emptyAttachment()
    }

}

object ImageUpdateProcessor : TelegramUpdateProcessor {
    override fun processAttachment(telegramClient: TelegramClient, token: String, update: Update): Attachment {
        return attachment(telegramClient, token, update.channelPost.photo.last().fileId, ContentType.IMAGE)
    }
}

object VideoUpdateProcessor : TelegramUpdateProcessor {
    override fun processAttachment(telegramClient: TelegramClient, token: String, update: Update): Attachment {
        return attachment(telegramClient, token, update.channelPost.video.fileId, ContentType.VIDEO)
    }
}

object AudioUpdateProcessor : TelegramUpdateProcessor {
    override fun processAttachment(telegramClient: TelegramClient, token: String, update: Update): Attachment {
        return attachment(telegramClient, token, update.channelPost.audio.fileId, ContentType.AUDIO)
    }
}

object DocumentUpdateProcessor : TelegramUpdateProcessor {
    override fun processAttachment(telegramClient: TelegramClient, token: String, update: Update): Attachment {
        return attachment(telegramClient, token, update.channelPost.document.fileId, ContentType.DOCUMENT)
    }
}

object VoiceUpdateProcessor : TelegramUpdateProcessor {
    override fun processAttachment(telegramClient: TelegramClient, token: String, update: Update): Attachment {
        return attachment(telegramClient, token, update.channelPost.voice.fileId, ContentType.VOICE)
    }
}

private fun getFile(telegramApi: TelegramClient, fileId: String): File {
    val getFile = GetFile.builder()
        .fileId(fileId)
        .build()
    return telegramApi.execute(getFile)
}


private fun attachment(telegramApi: TelegramClient, token: String, fileId: String, type: ContentType, vararg objects: Pair<String, Any>): Attachment {
    val file = getFile(telegramApi, fileId)
    val fileName = file
        .getFileUrl(token)
        .split("/")
        .last()
    val attachment = telegramApi.downloadFile(file.filePath)
    return Attachment(fileName, type, attachment.readBytes(), hashMapOf(*objects))
}