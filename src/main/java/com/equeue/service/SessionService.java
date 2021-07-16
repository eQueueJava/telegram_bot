package com.equeue.service;

import com.equeue.entity.Schedule;
import com.equeue.entity.Session;
import com.equeue.entity.User;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import com.equeue.repository.SessionRepository;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

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
                    "12.12.2021\n" +
                    "09:00";
        }
        String[] lines = messageTxt.split("\n");
        long userId = getUserIdFromSelectSession(lines, 1, ":", 1);
        long provId = getProvIdFromSelectSession(lines, 2, ":", 1);
        String date = lines[4].trim();
        String time = lines[5].trim();

        if (userRepository.findById(userId) == null) {
            return "User with id: " + userId + " not exist!";
        }
        if (providerRepository.findById(provId) == null) {
            return "Provider with id: " + userId + "not exist!";
        }
        if (!scheduleRepository.hasProviderAnySchedules(provId)) {
            return "This provider does not provide any services!";
        }
        if (!scheduleRepository.hasProviderScheduleSpecificDay(provId, date)) {
            return "The provider has no schedule for this day!";
        }

        Map<Long, Session> sessionByProviderOfDate = sessionRepository.findSessionByProviderOfDate(provId, date);
        for (Map.Entry<Long, Session> entry : sessionByProviderOfDate.entrySet()) {
            Long sessionId = entry.getKey();
            Session session = sessionByProviderOfDate.get(sessionId);
            if (HelperService.timeOf(session.getSessionStart().getTime()).equals(time)) {
                User customer = session.getCustomer();
                if (customer == null) {
                    User user = userRepository.findById(userId);
                    session.setCustomer(user);
                    List<Session> sessions = user.getSessions();
                    sessions.add(session);
                    return "Success! Session added from " + date + " " + time;
                } else {
                    return "This session is already busy!";
                }
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
                    "12.12.2021";
        }
        String[] lines = messageTxt.split("\n");
        final long providerId = getProviderIdSaveSessions(lines[1]);
        final String date = lines[3].trim();

        if (providerRepository.findById(providerId) == null) {
            return "There is no provider with that id: " + providerId + "!";
        }
        if (!scheduleRepository.hasProviderAnySchedules(providerId)) {
            return "This provider does not provide any services!";
        }
        Map<Long, Session> sessionByProvider = sessionRepository.findSessionByProviderOfDate(providerId, date);
        if (sessionByProvider.isEmpty()) {
            Schedule schedule;
            if (!scheduleRepository.hasProviderScheduleSpecificDay(providerId, date)) {
                return "The provider has no schedule for this day: " + date + "!";
            } else {
                schedule = scheduleRepository.getScheduleOfCurrentDay(providerId, date);
            }
            List<String> list = scheduleRepository.generateTemplate(schedule);
            addGeneratedSessions(lines[3], providerId, list);
        }

        Map<Long, Session> sessionByProviderOfDate = sessionRepository.findSessionByProviderOfDate(providerId, date);
        StringBuilder res = new StringBuilder();
        for (Map.Entry<Long, Session> entry : sessionByProviderOfDate.entrySet()) {
            Long sessionId = entry.getKey();
            Session session = sessionByProviderOfDate.get(sessionId);
            if (session.getCustomer() == null) {
                res.append(HelperService.timeOf(session.getSessionStart())).append('\n');
            }
        }
        return res.toString();
    }

    private void addGeneratedSessions(String line, long providerId, List<String> list) {
        for (int i = 1; i < list.size(); i++) {
            final Session session = new Session();
            session.setProvider(providerRepository.findById(providerId));
            session.setCustomer(null);
            session.setSessionStart(HelperService.stringToTimestamp(line.trim() + " " + list.get(i - 1).trim()));
            session.setSessionFinish(HelperService.stringToTimestamp(line.trim() + " " + list.get(i)));
            sessionRepository.save(session);
        }
    }

    private long getProviderIdSaveSessions(String line) {
        String[] rowTwo = line.split(" ");
        return Long.parseLong(rowTwo[2].trim());
    }

    private long getProvIdFromSelectSession(String[] lines, int i, String s, int i2) {
        String[] line3 = lines[i].split(s);
        return Long.parseLong(line3[i2].trim());
    }

    private long getUserIdFromSelectSession(String[] lines, int i, String s, int i2) {
        String[] lines2 = lines[i].split(s);
        return Long.parseLong(lines2[i2].trim());
    }
}
