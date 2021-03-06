package com.equeue.telegram_bot.messagesender;

import com.equeue.telegram_bot.EQueueBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageSenderImpl implements MessageSender {

    @Autowired
    private EQueueBot eQueueBot;

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            eQueueBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
