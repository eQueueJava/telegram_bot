package com.equeue.telegram_bot.services;


import com.equeue.api.controller.RegistrationController;
import com.equeue.service.ProviderService;
import com.equeue.service.ScheduleService;
import com.equeue.service.UserService;
import com.equeue.telegram_bot.Commands;
import com.equeue.telegram_bot.messagesender.MessageSender;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class SendMessageService {

    private final MessageSender messageSender;
    private final ScheduleService scheduleService;
    private final ProviderService providerService;
    private final UserService userService;

    public SendMessageService(MessageSender messageSender, ScheduleService scheduleService, ProviderService providerService, UserService userService) {
        this.messageSender = messageSender;
        this.scheduleService = scheduleService;
        this.providerService = providerService;
        this.userService = userService;
    }

    public void distribution(Message message) {
        String messageText = message.getText();

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

        switch (command) {
            case "/":
            case "/start":
                messageSender.sendMessage(getSendMessage(message, String.join("\n", Commands.commandMap.values())));
                break;
            case "/reg":
                messageSender.sendMessage(userRegistration(message));
                break;
            case Commands.CREATE_CLIENT:
                messageSender.sendMessage(getSendMessage(message, userService.save(message)));
                break;
            case Commands.CREATE_PROVIDER:
                messageSender.sendMessage(getSendMessage(message, providerService.save(message)));
                break;
            case Commands.CREATE_SCHEDULE:
                messageSender.sendMessage(getSendMessage(message, scheduleService.save(message)));
                break;
            case Commands.READ_CLIENT:
                messageSender.sendMessage(getSendMessage(message, userService.findById(message)));
                break;
            case Commands.READ_PROVIDER:
                messageSender.sendMessage(getSendMessage(message, providerService.findById(message)));
                break;
            case "/info":
                messageSender.sendMessage(getInfo(message));
                break;
            default:
                messageSender.sendMessage(defaultMessage(message));
        }
    }

    private SendMessage userRegistration(Message message) {
        return getSendMessage(
                message,
                RegistrationController.createUser(message)
        );
    }

    private SendMessage getSendMessage(Message message, String test) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(test);
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
