package com.equeue.telegram_bot;

import com.equeue.telegram_bot.services.ButtonProcessingService;
import com.equeue.telegram_bot.services.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class EQueueBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;

    private SendMessageService sendMessageService;
    private ButtonProcessingService buttonProcessingService;

    @Autowired
    public void setSendMessageService(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Autowired
    public void setButtonProcessingService(ButtonProcessingService buttonProcessingService) {
        this.buttonProcessingService = buttonProcessingService;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()){
            var callbackQuery = update.getCallbackQuery();
            if(callbackQuery.getData() != null){
                buttonProcessingService.distribution(callbackQuery);
            }
        }
        if (update.hasMessage()) {
            var message = update.getMessage();
            if (message.hasText()) {
                sendMessageService.distribution(message);
            }
        }
    }
}
