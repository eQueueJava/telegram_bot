package com.equeue.service;

import com.equeue.entity.User;
import com.equeue.entity.enumeration.UserRole;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.Commands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    ProviderRepository providerRepository;
    @InjectMocks
    UserService userService;

    @Test
    void save() {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(1L);
        message.setChat(chat);
        message.setText(Commands.CREATE_CLIENT);

        Assertions.assertEquals("Введите данные в виде:\n" + Commands.CREATE_CLIENT + "\nname: Donald Trump",
                userService.save(message));

        when(userRepository.findByName(anyString())).thenReturn(new User());
        when(userRepository.findByTelegramId(anyLong())).thenReturn(new User().setUserRole(UserRole.GUEST));
        when(userRepository.save(any())).thenReturn(new User());
        message.setText(Commands.CREATE_CLIENT + "\nname: Donald Trump");
        Assertions.assertEquals("Поздравляю вы зарегистрировались!\nВаше имя : Donald Trump",
                userService.save(message));
    }

    @Test
    void failSave(){
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(1L);
        message.setChat(chat);
        message.setText(Commands.CREATE_CLIENT + "\nname: Donald Trump!");
        Assertions.assertEquals("Не правильно введено имья!", userService.save(message));

        when(userRepository.findByName(anyString())).thenReturn(new User().setId(1L));
        message.setText(Commands.CREATE_CLIENT + "\nname: Donald Trump");
        Assertions.assertEquals("Пользователь c таким именем уже зарегистрировался!",
                userService.save(message));

        when(userRepository.findByName(anyString())).thenReturn(new User());
        when(userRepository.findByTelegramId(anyLong())).thenReturn(new User().setUserRole(UserRole.CLIENT).setName("Donald"));
        message.setText(Commands.CREATE_CLIENT + "\nname: Donald Trump");
        Assertions.assertEquals("Вы уже были зарегистрированы ранее! \nВаше имя: Donald",
                userService.save(message));
    }

    @Test
    void deleteUser() {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(1L);
        message.setChat(chat);
        when(userRepository.findByTelegramId(anyLong())).thenReturn(new User().setId(1L));
        when(providerRepository.deleteAllProvidersForUser(any())).thenReturn(new ArrayList<>());
        when(userRepository.deleteUser(anyLong())).thenReturn(new User());
        Assertions.assertEquals("Вы удалили все свои данные!", userService.deleteUser(message));
    }

    @Test
    void failDeleteUser() {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(1L);
        message.setChat(chat);
        when(userRepository.findByTelegramId(anyLong())).thenReturn(new User());
        Assertions.assertEquals("Вы не зарегистрированы!", userService.deleteUser(message));
    }

    @Test
    void findById() {
        Message message = new Message();
        message.setText(Commands.READ_CLIENT);

        Assertions.assertEquals("Введите данные в виде:\n" +
                Commands.READ_CLIENT + "\n" +
                "clientName: Donald Trump",
                userService.findById(message));

        message.setText(Commands.READ_CLIENT + "\nclientName: Donald Trump");
        when(userRepository.findByName(anyString())).thenReturn(new User());
        Assertions.assertEquals("Ваше имя - 'null' \nВы зарегистрированы как - 'null' \nВаш часовой пояс - null \n",
                userService.findById(message));
    }
}
