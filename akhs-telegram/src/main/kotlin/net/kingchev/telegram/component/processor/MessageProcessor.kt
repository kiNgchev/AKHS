package net.kingchev.telegram.component.processor

import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.media.InputMedia
import org.telegram.telegrambots.meta.generics.TelegramClient

interface MessageProcessor {
    fun process(telegramApi: TelegramClient, attachments: List<InputMedia>, chatId: String, text: String)

    companion object {
        private val processors = { size: Int -> when(size) {
            0 -> EmptyMessageProcessor
            1 -> MessageWithMediaProcessor
            else -> MessageWithMediasProcessor
        } }

        fun getProcessor(size: Int): MessageProcessor = processors(size)
    }
}

object EmptyMessageProcessor : MessageProcessor {
    override fun process(telegramApi: TelegramClient, attachments: List<InputMedia>, chatId: String, text: String) {
        val post = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .build()
        telegramApi.execute(post)
    }
}

object MessageWithMediaProcessor : MessageProcessor {
    override fun process(telegramApi: TelegramClient, attachments: List<InputMedia>, chatId: String, text: String) {}
}

object MessageWithMediasProcessor : MessageProcessor {
    override fun process(telegramApi: TelegramClient, attachments: List<InputMedia>, chatId: String, text: String) {
        val post = SendMediaGroup.builder()
            .chatId(chatId)
            .medias(attachments)
            .build()
        telegramApi.execute(post)
    }
}