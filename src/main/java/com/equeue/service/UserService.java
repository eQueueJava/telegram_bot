package com.equeue.service;

import com.equeue.entity.Provider;
import com.equeue.entity.User;
import com.equeue.entity.enumeration.UserRole;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import com.equeue.repository.SessionRepository;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.ButtonCommands;
import com.equeue.telegram_bot.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserService {
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("Europe/Kiev");

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    ScheduleRepository scheduleRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public User deleteUser(Long id) {
        return userRepository.deleteUser(id);
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
        User currentUser = findByTelegramId(message);
        if (currentUser.getUserRole() == UserRole.CLIENT) {
            return "Вы уже были зарегистрированы ранее! \n" +
                    "Ваше имя: " + currentUser.getName();
        }

        currentUser.setName(name).setUserRole(UserRole.CLIENT);
        save(currentUser);
        return "Поздравляю вы зарегистрировались!\n" +
                "Ваше имя : " + name;
    }

    public String deleteUser(Message message) {
        User user = findByTelegramId(message);
        Long id = user.getId();
        if (id == null) {
            return "Вы не зарегистрированы!";
        }

        List<Provider> providers = providerRepository.deleteAllProvidersForUser(user);
        for (Provider provider : providers) {
            sessionRepository.deleteAllForProvider(provider);
            scheduleRepository.deleteByProvider(provider);
        }
        deleteUser(id);
        return "Вы удалили все свои данные!";
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
        Long userId = Long.valueOf(lines[1].replace("clientId:", "").trim());
        return userRepository.findById(userId).toString();
    }

    private User findByName(String name) {
        return userRepository.findByName(name);
    }

    public User findByTelegramId(Message message) {
        Long id = message.getFrom().getId();
        return userRepository.findByTelegramId(id);
    }

    public void registerGuestUserIfNotExist(Message message) {
        Long tgId = message.getFrom().getId();
        if (userRepository.findByTelegramId(tgId) == null) {
            String tgUsername = message.getFrom().getUserName();
            userRepository.save(new User()
                    .setName(tgUsername)
                    .setUserRole(UserRole.GUEST)
                    .setTelegramId(tgId)
                    .setZoneId(DEFAULT_ZONE)
                    .setTelegramUsername(tgUsername));
        }
    }

    public SendMessage askOrDeleteUser(Message message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
        buttonYes.setText("Да!");
        buttonYes.setCallbackData(ButtonCommands.DELETE_CLIENT_YES);
        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
        buttonNo.setText("Нет!");
        buttonNo.setCallbackData(ButtonCommands.DELETE_CLIENT_NO);

        List<InlineKeyboardButton> keyboardButtonList = new ArrayList<>();
        keyboardButtonList.add(buttonYes);
        keyboardButtonList.add(buttonNo);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonList);
        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText("Вы точно хотите удалить свой профиль?");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public SendMessage askCurrentUserTime(Message message) {
        List<LocalTime> allAvailableTime = TimeUtil.getAllAvailableTime();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonList = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        int iconInRow = 3;
        for (LocalTime time: allAvailableTime) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(time.format(DateTimeFormatter.ofPattern(TimeUtil.TIME_PATTERN)));
            button.setCallbackData(ButtonCommands.SET_TIME + HelperService.PARAM_DIVIDER +
                    time.format(DateTimeFormatter.ofPattern(TimeUtil.TIME_PATTERN)));
            keyboardButtonList.add(button);
            if(keyboardButtonList.size() == iconInRow){
                rowList.add(keyboardButtonList);
                keyboardButtonList = new ArrayList<>();
            }
        }
        InlineKeyboardButton buttonCancel = new InlineKeyboardButton();
        buttonCancel.setText("Отмена!");
        buttonCancel.setCallbackData(ButtonCommands.SET_TIME + HelperService.PARAM_DIVIDER + ButtonCommands.CANCEL);
        keyboardButtonList.add(buttonCancel);
        if(!keyboardButtonList.isEmpty()){
            rowList.add(keyboardButtonList);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText("Какое у вас сейчас время?");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    public SendMessage askCurrentUserTimezone(Message message, String timeString) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime userLocalDateTime = LocalDateTime.of(now.toLocalDate(), LocalTime.parse
                (timeString, DateTimeFormatter.ofPattern(TimeUtil.TIME_PATTERN)));
        Set<String> zones = new TreeSet<>();
        for (String id : ZoneId.getAvailableZoneIds()) {
            ZoneId zone = ZoneId.of(id);
            ZoneOffset offset = zone.getRules().getOffset(now);
            if (Math.abs(userLocalDateTime.toInstant(ZoneOffset.UTC).
                    getEpochSecond() - Instant.now().getEpochSecond() - offset.getTotalSeconds()) < 600) {
                zones.add(zone.toString());
            }
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonList = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        int iconInRow = 2;
        for (String zone: zones) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(zone);
            button.setCallbackData(ButtonCommands.SET_TIMEZONE + HelperService.PARAM_DIVIDER + zone);
            keyboardButtonList.add(button);
            if(keyboardButtonList.size() == iconInRow){
                rowList.add(keyboardButtonList);
                keyboardButtonList = new ArrayList<>();
            }
        }
        InlineKeyboardButton buttonCancel = new InlineKeyboardButton();
        buttonCancel.setText("Отмена!");
        buttonCancel.setCallbackData(ButtonCommands.SET_TIMEZONE + HelperService.PARAM_DIVIDER + ButtonCommands.CANCEL);
        keyboardButtonList.add(buttonCancel);
        if(!keyboardButtonList.isEmpty()){
            rowList.add(keyboardButtonList);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText("Какой у вас часовой пояс?");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    public String setCurrentUserTimezone(Message message, String zone){
        try {
            User byId = userRepository.findByTelegramId(message.getChatId());
            byId.setZoneId(ZoneId.of(zone));
        } catch (Exception e) {
            return "FAIL";
        }
        return "OK";
    }
}
