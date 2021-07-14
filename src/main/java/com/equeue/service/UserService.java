package com.equeue.service;

import com.equeue.entity.User;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
public class UserService {

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
        User client = new User()
                .setName(lines[1].replace("name:", "").trim())
                .setRole("CLIENT")
                .setTelegramId(message.getFrom().getId());
        save(client);
        return client.toString();
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

}
