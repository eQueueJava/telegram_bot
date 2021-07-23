package com.equeue.telegram_bot.services;

import com.equeue.service.ProviderService;
import com.equeue.service.ScheduleService;
import com.equeue.service.SessionService;
import com.equeue.service.UserService;
import com.equeue.telegram_bot.Commands;
import com.equeue.telegram_bot.messagesender.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class SendMessageService {

    @Autowired
    private MessageSender messageSender;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;

    public void distribution(Message message) {
        userService.registerGuestUserIfNotExist(message);

        String command = getCommand(message.getText());

        switch (command) {
            case "/":
            case "/start":
                messageSender.deleteMessage(getSendMessage(message, String.join("\n", Commands.getCommandMap().values())));
                break;
            case Commands.SHOW_CURRENT_USER_INFO:
                messageSender.sendMessage(getSendMessage(message, userService.findByTelegramId(message).toString()));
                break;
            case Commands.CREATE_CLIENT:
                messageSender.deleteMessage(getSendMessage(message, userService.save(message)));
                break;
            case Commands.CREATE_PROVIDER:
                messageSender.deleteMessage(getSendMessage(message, providerService.save(message)));
                break;
            case Commands.CREATE_SCHEDULE:
                messageSender.deleteMessage(getSendMessage(message, scheduleService.save(message)));
                break;
            case Commands.READ_CLIENT:
                messageSender.deleteMessage(getSendMessage(message, userService.findById(message)));
                break;
            case Commands.READ_PROVIDER:
                messageSender.deleteMessage(getSendMessage(message, providerService.findById(message)));
                break;
            case Commands.GET_FREE_TIME:
                messageSender.deleteMessage(getSendMessage(message, sessionService.saveSession(message)));
                break;
            case Commands.INPUT_TIME:
                messageSender.deleteMessage(getSendMessage(message, sessionService.selectSession(message)));
                break;
            case Commands.DELETE_CLIENT:
                messageSender.deleteMessage(userService.askOrDeleteUser(message));
                break;
            default:
                messageSender.deleteMessage(defaultMessage(message));
        }
    }

    private String getCommand(String messageText) {
        String command;
        if (messageText.indexOf(' ') > 0) {
            if (messageText.indexOf('\n') > 0 && messageText.indexOf('\n') < messageText.indexOf(' ')) {
                command = messageText.substring(0, messageText.indexOf('\n'));
            } else {
                command = messageText.substring(0, messageText.indexOf(' '));
            }
        } else if (messageText.indexOf('\n') > 0) {
            command = messageText.substring(0, messageText.indexOf('\n'));
        } else {
            command = messageText;
        }
        return command;
    }

    private SendMessage getSendMessage(Message message, String text) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(text);
        return sendMessage;
    }

    private SendMessage defaultMessage(Message message) {
        return SendMessage.builder()
                .text("Неизвестная команда!")
                .chatId(String.valueOf(message.getChatId()))
                .build();
    }

    public void sendTextTo(String messageTest, Long telegramId) {
        messageSender.sendTextTo(messageTest, telegramId);
    }

}
