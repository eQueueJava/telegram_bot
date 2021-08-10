package com.equeue.IntegrationTests;

import com.equeue.service.UserService;
import com.equeue.telegram_bot.Commands;
import com.equeue.telegram_bot.EQueueBot;
import com.equeue.telegram_bot.messagesender.MessageSender;
import com.equeue.telegram_bot.messagesender.MessageSenderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class IntegrationTest {
    @Autowired
    EQueueBot eQueueBot;

    @Mock
    UserService userService;

    @Mock
    MessageSenderImpl messageSender;

    @Test
    void firstTest(){
        Update update = getNewUpdate(1L, Commands.CREATE_CLIENT + "\nname: Donald Trump");
        eQueueBot.onUpdateReceived(update);
        doNothing().when(messageSender).sendMessage(isA(SendMessage.class));
        Mockito.verify(userService, Mockito.times(1)).save((com.equeue.entity.User) any());
    }

    private Update getNewUpdate(Long id, String request) {
        Update update = new Update();

        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(id);
        chat.setUserName("TestUser " + id);
        User userFrom = new User();
        userFrom.setId(id);
        userFrom.setUserName("TestUser(From) " + id);

        message.setFrom(userFrom);
        message.setChat(chat);
        message.setText(request);
        update.setMessage(message);

        return update;
    }
}
