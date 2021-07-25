package com.equeue.telegram_bot.services;

import com.equeue.service.UserService;
import com.equeue.telegram_bot.ButtonCommands;
import com.equeue.telegram_bot.messagesender.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class ButtonProcessingService {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSender messageSender;

    public void distribution(CallbackQuery callbackQuery) {
        String command = callbackQuery.getData();
        Message message = callbackQuery.getMessage();

        switch (command){
            case ButtonCommands.DELETE_CLIENT_YES:
                messageSender.sendMessage(getSendMessage(message, userService.deleteUser(message)));
                break;
            case ButtonCommands.DELETE_CLIENT_NO:
                messageSender.deleteMessage(deleteMessage(message));
                break;
            case ButtonCommands.SET_TIMEZONE:
                break;
            default:
                messageSender.sendMessage(getSendMessage(message, "Неизвестная кнопка!"));
        }
    }

    private DeleteMessage deleteMessage(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        return deleteMessage;
    }

    private SendMessage getSendMessage(Message message, String text) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(text);
        return sendMessage;
    }
}
