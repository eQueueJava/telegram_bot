package com.equeue.telegram_bot.messagesender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {
    void sendTextTo(String messageText, Long telegramId);

    void sendMessage(SendMessage sendMessage);
}
