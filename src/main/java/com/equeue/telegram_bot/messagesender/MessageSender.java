package com.equeue.telegram_bot.messagesender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

public interface MessageSender {
    void sendTextTo(String messageText, Long telegramId);

    void sendMessage(SendMessage sendMessage);

    void deleteMessage(DeleteMessage deleteMessage);
}
