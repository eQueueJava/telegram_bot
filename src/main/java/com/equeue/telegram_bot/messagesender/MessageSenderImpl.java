package com.equeue.telegram_bot.messagesender;

import com.equeue.telegram_bot.EQueueBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageSenderImpl implements MessageSender {

    @Autowired
    private EQueueBot eQueueBot;

    @Override
    public void sendTextTo(String messageText, Long telegramId) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(telegramId));
        sendMessage.setText(messageText);
        try {
            eQueueBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            eQueueBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessage(DeleteMessage deleteMessage) {
        try {
            eQueueBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
