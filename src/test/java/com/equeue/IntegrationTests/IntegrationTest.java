package com.equeue.IntegrationTests;

import com.equeue.service.UserService;
import com.equeue.telegram_bot.Commands;
import com.equeue.telegram_bot.EQueueBot;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootTest
public class IntegrationTest {
    @Autowired
    EQueueBot eQueueBot;

    @Mock
    UserService userService;

    @Test
    void firstTest(){
        Update update = getNewUpdate(1L, Commands.CREATE_CLIENT + "\nname: Donald Trump");
        eQueueBot.onUpdateReceived(update);

        Mockito.verify(userService, Mockito.times(1));
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
