package com.equeue.service;

import com.equeue.entity.User;
import com.equeue.entity.enumeration.UserRole;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    public static final String ZONE_ID_KYIV = "Europe/Kiev";
    public static final String PARAM_DIVIDER = "__";

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
        User currentUser = findByTelegramId(message);
        if (currentUser.getUserRole() == UserRole.CLIENT) {
            return "Вы уже были зарегистрированы ранее! \n" +
                    "Ваше имя: " + currentUser.getName();
        }

        User client = new User()
                .setName(name)
                .setUserRole(UserRole.CLIENT)
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

    public User findByTelegramId(Message message) {
        Long id = message.getChatId();
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
                    .setTelegramUsername(tgUsername));
        }
    }

    public String setCurrentUserTimezone(Message message) {
        String messageText = message.getText();
        String[] params = getParams(messageText);
        if (params.length == 0) {
            List<LocalTime> allAvailableTime = TimeUtil.getAllAvailableTime();
            return allAvailableTime.stream()
                    .map(t -> t.format(DateTimeFormatter.ofPattern(TimeUtil.TIME_PATTERN)))
                    .map(s -> Commands.SET_CURRENT_USER_TIMEZONE + PARAM_DIVIDER + s.replace(":","_"))
                    .collect(Collectors.joining("\n"));
        }
        if (params[0].matches("\\d?\\d_\\d\\d")) {
            String timeString = params[0].replace("_", ":");

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime userLocalDateTime = LocalDateTime.of(now.toLocalDate(),LocalTime.parse(timeString,DateTimeFormatter.ofPattern(TimeUtil.TIME_PATTERN)));
            Set<ZoneId> zones = new HashSet<>();
            for (String id : ZoneId.getAvailableZoneIds()) {
                ZoneId zone = ZoneId.of(id);
                ZoneOffset offset = zone.getRules().getOffset(now);
                if (Math.abs(userLocalDateTime.toInstant(ZoneOffset.UTC).getEpochSecond() - Instant.now().getEpochSecond() - offset.getTotalSeconds()) < 600) {
                    zones.add(zone);
                }
            }
            return zones.stream()
                    .map(ZoneId::toString)
                    .sorted()
                    .map(z-> Commands.SET_CURRENT_USER_TIMEZONE + PARAM_DIVIDER + z.replace("/","_"))
                    .collect(Collectors.joining("\n"));
        }
        String zone = params[0].replace("_", "/");
        try {
            User byId = userRepository.findByTelegramId(message.getChatId());
            byId.setZoneId(ZoneId.of(zone));
        } catch (Exception e) {
            return "FAIL";
        }
        return "OK";
    }

    private String[] getParams(String command) {
        String[] strings = command.split("__");
        return Arrays.copyOfRange(strings, 1, strings.length);
    }

}
