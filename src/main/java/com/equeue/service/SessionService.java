package com.equeue.service;

import com.equeue.entity.Provider;
import com.equeue.entity.Schedule;
import com.equeue.entity.Session;
import com.equeue.entity.User;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import com.equeue.repository.SessionRepository;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.Commands;
import com.equeue.telegram_bot.services.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    SendMessageService sendMessageService;

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public Session findById(Long id) {
        return sessionRepository.findById(id);
    }

    public String selectSession(Message message) {
        String messageTxt = message.getText();
        if (messageTxt.replace(Commands.INPUT_TIME, "").isBlank()) {
            return "Введите данные в виде:\n" +
                    Commands.INPUT_TIME + "\n" +
                    "user iD: 1\n" +
                    "provider Id: 1\n" +
                    "date:\n" +
                    "12.07.2021\n" +
                    "11:30";
        }
        String[] lines = messageTxt.split("\n");
        long userId = getIdFromSelectSession(lines, 1, ":", 1);
        long provId = getIdFromSelectSession(lines, 2, ":", 1);
        String date = lines[4].trim();
        LocalDate dateFromString = TimeUtil.getDateFromString(date);
        String time = lines[5].trim();
        LocalTime timeFromString = TimeUtil.getTimeFromString(time);

        User userById = userRepository.findById(userId);
        if (userById == null) {
            return "User with id: " + userId + " not exist!";
        }
        if (providerRepository.findById(provId) == null) {
            return "Provider with id: " + userId + "not exist!";
        }
        if (scheduleRepository.findAllByProvider(provId).isEmpty()) {
            return "This provider does not provide any services!";
        }
        int dayOfWeek = dateFromString.getDayOfWeek().getValue();
        if (scheduleRepository.findByProviderAndDayOfWeek(provId, dayOfWeek) == null) {
            return "The provider has no schedule for this day!";
        }

        LocalDateTime userDateTime = LocalDateTime.of(TimeUtil.localDateFromString(date), TimeUtil.localTimeFromString(time));
        LocalDateTime utcDateTime = TimeUtil.utcDateTimeFromLocalDateTimeAndZone(userDateTime, userById.getZoneId());

        List<Session> sessionsByProviderAndDate = sessionRepository.findByProviderAndDate(provId, dateFromString);
        for (Session session : sessionsByProviderAndDate) {
            if (session.getSessionStart().toLocalTime().equals(utcDateTime)) {
                if (session.getCustomer() != null) {
                    return "This session is already busy!";
                }
                User customer = userRepository.findByTelegramId(message.getFrom().getId());
                session.setCustomer(customer);
                List<Session> sessions = customer.getSessions();
                sessions.add(session);
                String messageForProvider = "New record: @" + session.getCustomer().getTelegramUsername() +
                        "(t.me/" + session.getCustomer().getTelegramUsername() + ")" +
                        "\nProvider: " + session.getProvider().getName() +
                        "\nSession: " + TimeUtil.getStringFromDateTime(session.getSessionStart());
                sendMessageService.sendTextTo(messageForProvider, session.getProvider().getClient().getTelegramId());
                return "Success! Session added from " + date + " " + time;
            }
        }
        return "This session does not exist!";
    }

    public String saveSession(Message message) {
        String messageTxt = message.getText();
        if (messageTxt.replace(Commands.GET_FREE_TIME, "").isBlank()) {
            return "Введите данные в виде:\n" +
                    Commands.GET_FREE_TIME + "\n" +
                    "provider Id: 1\n" +
                    "date:\n" +
                    "12.07.2021";
        }
        String[] lines = messageTxt.split("\n");
        final long providerId = getProviderIdSaveSessions(lines[1]);
        final String date = lines[3].trim();
        LocalDate dateFromString = TimeUtil.getDateFromString(date);

        if (providerRepository.findById(providerId) == null) {
            return "There is no provider with that id: " + providerId + "!";
        }
        if (scheduleRepository.findAllByProvider(providerId).isEmpty()) {
            return "This provider does not provide any services!";
        }
        List<Session> sessionByProvider = sessionRepository.findByProviderAndDate(providerId, dateFromString);
        if (sessionByProvider.isEmpty()) {
            List<Session> sessions = getSessions(providerId, dateFromString);
            if (sessions.isEmpty()) {
                return "The provider has no schedule for this day: " + date + "!";
            }
            addGeneratedSessions(sessions, dateFromString);
        }

        List<Session> sessionByProviderAndDate = sessionRepository.findByProviderAndDate(providerId, dateFromString);
        StringBuilder res = new StringBuilder();
        for (Session session : sessionByProviderAndDate) {
            if (session.getCustomer() == null) {
                res.append(TimeUtil.getStringFromTime(session.getSessionStart().toLocalTime())).append('\n');
            }
        }
        return res.toString();
    }

    private List<Session> getSessions(long providerId, LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        Schedule schedule = scheduleRepository.findByProviderAndDayOfWeek(providerId, dayOfWeek);
        if (schedule == null) {
            return new ArrayList<>();
        }
        Provider provider = schedule.getProvider();
        Integer duration = schedule.getDuration();
        LocalTime workFinish = schedule.getWorkFinish();
        LocalTime sessionStart = schedule.getWorkStart();
        LocalTime sessionFinish = sessionStart.plusMinutes(duration);
        List<Session> generatedSessions = new ArrayList<>();
        while (sessionFinish.compareTo(workFinish) <= 0) {
            generatedSessions.add(new Session()
                    .setProvider(provider)
                    .setSessionStart(sessionStart.atDate(date))
                    .setSessionFinish(sessionFinish.atDate(date)));
            sessionStart = sessionFinish;
            sessionFinish = sessionStart.plusMinutes(duration);
        }
        return generatedSessions;
    }

    private void addGeneratedSessions(List<Session> sessions, LocalDate date) {
        for (Session session : sessions) {
            session.setSessionStart(session.getSessionStart().with(date));
            session.setSessionFinish(session.getSessionFinish().with(date));
            sessionRepository.save(session);
        }
    }

    private long getProviderIdSaveSessions(String line) {
        String[] rowTwo = line.split(" ");
        return Long.parseLong(rowTwo[2].trim());
    }

    private long getIdFromSelectSession(String[] lines, int i, String s, int i2) {
        String[] line3 = lines[i].split(s);
        return Long.parseLong(line3[i2].trim());
    }

}
