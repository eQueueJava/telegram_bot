package com.equeue.service;

import com.equeue.entity.Provider;
import com.equeue.entity.User;
import com.equeue.entity.enumeration.UserRole;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
public class ProviderService {

    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    UserRepository userRepository;

    public Provider save(Provider provider) {
        return providerRepository.save(provider);
    }

    public List<Provider> findAll() {
        return providerRepository.findAll();
    }

    public Provider findById(Long id) {
        return providerRepository.findById(id);
    }

    public String save(Message message) {
        String messageText = message.getText();
        User user = userRepository.findByTelegramId(message.getChatId());
        if(user.getUserRole().equals(UserRole.GUEST)){
            return "Только зарегистрированные пользователи могут предоставлять услуги!";
        }
        if (messageText.replace(Commands.CREATE_PROVIDER, "").isBlank()) {
            return "Введите данные в виде:\n" +
                    Commands.CREATE_PROVIDER + "\n" +
                    "name: BarberShop";
        }

        String[] lines = messageText.split("\n");
        Provider provider = new Provider()
                .setClient(user)
                .setName(lines[1].replace("name:", "").trim());
        save(provider);
        return provider.toString();
    }

    public String findByName(Message message) {
        String messageText = message.getText();
        if (messageText.replace(Commands.READ_PROVIDER, "").isBlank()) {
            return "Введите данные в виде:\n" +
                    Commands.READ_PROVIDER + "\n" +
                    "providerName: BarberShop";
        }

        String[] lines = messageText.split("\n");
        return providerRepository.findByName(lines[1].replace("providerName:", "").trim()).toString();
    }
}
