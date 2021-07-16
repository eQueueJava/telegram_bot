package com.equeue.api.checkers;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

public class
RegistrationChecker {
    private RegistrationChecker() {}

    public static boolean isUser(Map<Long, String> USERS_CACHE , String userName) {
        for (Map.Entry<Long, String> entry: USERS_CACHE.entrySet()) {
            if(entry.getValue().equals(userName)){
                return false;
            }
        }
        return true;
    }

    public static boolean isValidRequest(Message message){
        String request = message.getText();
        return request.matches("^\\/reg @\\S*");
    }
}
