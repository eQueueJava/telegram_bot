package com.equeue.service;

import com.equeue.entity.User;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.ZoneId;
import java.util.List;

@Service
public class UserService {
    public static final String ZONE_ID_KYIV = "Europe/Kiev";

    @Autowired
    UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public String save(Message message) {
        String messageText = message.getText();
        if (messageText.replace(Commands.CREATE_CLIENT, "").isBlank()) {
            return "Введите данные в виде:\n" +
                    Commands.CREATE_CLIENT + "\n" +
                    "name: Donald Trump";
        }

        String[] lines = messageText.split("\n");
        String name = lines[1].replace("name:", "").trim();

        if (!checkName(name)) {
            return "Не правильно введено имья!";
        }
        if (findByName(name).getId() != null) {
            return "Пользователь c таким именем уже зарегистрировался!";
        }
        if (findByTelegramId(message).getTelegramId() != null) {
            return "Вы уже зарегистрированы!";
        }

        User client = new User()
                .setName(name)
                .setRole("CLIENT")
                .setTelegramId(message.getChatId())
                .setZoneId(ZoneId.of(ZONE_ID_KYIV));
        save(client);
        return "Поздравляю вы зарегистрировались!\n" +
                "Ваше имя : " + name;
    }

    private boolean checkName(String name) {
        return name.matches("^[a-zA-Z0-9- ]+$");
    }

    public String findById(Message message) {
        String messageText = message.getText();
        if (messageText.replace(Commands.READ_CLIENT, "").isBlank()) {
            return "Введите данные в виде:\n" +
                    Commands.READ_CLIENT + "\n" +
                    "clientId: 1";
        }

        String[] lines = messageText.split("\n");
        return userRepository.findById(Long.valueOf(lines[1].replace("clientId:", "").trim())).toString();
    }

    private User findByName(String name) {
        return userRepository.findByName(name);
    }

    private User findByTelegramId(Message message) {
        Long id = message.getChatId();
        return userRepository.findByTelegramId(id);
    }
}
