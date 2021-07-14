package com.equeue.api.controller;

import com.equeue.api.checkers.RegistrationChecker;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistrationController {
    private RegistrationController() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<Long, String> USERS_CACHE = new HashMap<>();


    public static String createUser(Message message) {
        if (RegistrationChecker.isValidRequest(message)) {

            Long chatId = message.getChatId();
            String userName = message.getText().toLowerCase(Locale.ROOT);
            userName = userName.substring(userName.lastIndexOf('@'));

            if (RegistrationChecker.isUser(USERS_CACHE, userName)) {
                USERS_CACHE.put(chatId, userName);
                return "Поздоровляю ви зареєструвалися!";
            }
            return "Користувач з таким логіном вже існує!";
        }
        return "Не коректно виконаний запит!";
    }
}
