package com.equeue.telegram_bot.services;


import com.equeue.api.controller.RegistrationController;
import com.equeue.telegram_bot.messagesender.MessageSender;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class SendMessageService {
    private final MessageSender messageSender;

    public SendMessageService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void distribution(Message message) {
        String messageText = message.getText();

        if (messageText.startsWith("/reg")) {
            messageSender.sendMessage(userRegistration(message));
            return;
        }

        switch (messageText) {
            case "/info":
                messageSender.sendMessage(getInfo(message));
                break;
            default:
                messageSender.sendMessage(defaultMessage(message));
        }
    }

    private SendMessage userRegistration(Message message) {
        var sendMessage = new SendMessage();
        sendMessage.setText(RegistrationController.createUser(message));
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        return sendMessage;
    }

    private SendMessage getInfo(Message message) {
        return SendMessage.builder()
                .text("Інформація")
                .chatId(String.valueOf(message.getChatId()))
                .build();
    }

    private SendMessage defaultMessage(Message message) {
        return SendMessage.builder()
                .text("Не вірна команда!")
                .chatId(String.valueOf(message.getChatId()))
                .build();
    }
}
