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
import java.util.Map;

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
        String name = lines[1].replace("name:", "").trim();
        if(isName(name, user)){
            return "Вы уже зарегистрировали заведение с таким именем!";
        }
        Provider provider = new Provider()
                .setClient(user)
                .setName(name);
        save(provider);
        return provider.toString();
    }

    private boolean isName(String name, User user) {
        Map<Long, Provider> allProvidersForUser = providerRepository.findAllByUser(user);
        for (Map.Entry<Long, Provider> entry: allProvidersForUser.entrySet()) {
            if(entry.getValue().getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public String findForUserByName(Message message) {
        Map<String, String> request = HelperService.parseRequest(message.getText());
        if (request.size() == 1) {
            return "Введите данные в виде:\n" +
                    Commands.READ_PROVIDER + "\n" +
                    "userName: Donald Trump\n" +
                    "providerName: BarberShop";
        }
        User user = userRepository.findByName(request.get("userName"));

        if(!request.containsKey("providerName")){
            if(user.getProviders().isEmpty()){
                return "У этого пользователя нету представителей услуг!";
            }

            if(user.getProviders().size() == 1){
                return user.getProviders().get(0).toString();
            }

            return "Укажите providerName!";
        }

        for (Provider provider: user.getProviders()) {
            if(provider.getName().equals(request.get("providerName"))){
                return provider.toString();
            }
        }
        return "У вас нету услуги с таким именем!";
    }
}
