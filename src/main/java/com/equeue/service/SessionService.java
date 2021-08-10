package com.equeue.service;

import com.equeue.entity.Provider;
import com.equeue.entity.Schedule;
import com.equeue.entity.Session;
import com.equeue.entity.User;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import com.equeue.repository.SessionRepository;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.ButtonCommands;
import com.equeue.telegram_bot.Commands;
import com.equeue.telegram_bot.services.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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

    public String selectSession(Message message, String time, Long providerId, String date) {
        User userById = userRepository.findByTelegramId(message.getChatId());
        if (providerRepository.findById(providerId) == null) {
            return "Provider with id: " + providerId + "not exist!";
        }
        if (scheduleRepository.findAllByProvider(providerId).isEmpty()) {
            return "This provider does not provide any services!";
        }

        LocalDate dateFromString = TimeUtil.getDateFromString(date);
        int dayOfWeek = dateFromString.getDayOfWeek().getValue();
        if (scheduleRepository.findByProviderAndDayOfWeek(providerId, dayOfWeek) == null) {
            return "The provider has no schedule for this day!";
        }

        LocalDateTime userDateTime = LocalDateTime.of(TimeUtil.getDateFromString(date), TimeUtil.getTimeFromString(time));
        LocalDateTime utcDateTime = TimeUtil.getUtcDateTimeFromDateTimeAndZone(userDateTime, userById.getZoneId());

        List<Session> sessionsByProviderAndDate = sessionRepository.findByProviderAndDate(providerId, dateFromString);
        for (Session session : sessionsByProviderAndDate) {
            if (session.getSessionStart().toLocalTime().equals(utcDateTime.toLocalTime())) {
                if (session.getCustomer() != null) {
                    return "This session is already busy!";
                }
                User customer = userRepository.findByTelegramId(message.getChatId());
                session.setCustomer(customer);
                List<Session> sessions = customer.getSessions();
                sessions.add(session);
                String messageForProvider = "New record: @" + session.getCustomer().getTelegramUsername() +
                        "(t.me/" + session.getCustomer().getTelegramUsername() + ")" +
                        "\nProvider: " + session.getProvider().getName() +
                        "\nSession: " + TimeUtil.getStringFromDateTime(
                        TimeUtil.getDateTimeFromUtcDateTimeForZone(
                                session.getSessionStart(),
                               userById.getZoneId()));
                sendMessageService.sendTextTo(messageForProvider, session.getProvider().getClient().getTelegramId());
                return "Success! Session added from " + date + " " + time;
            }
        }
        return "This session does not exist!";
    }

    public SendMessage saveSession(Message message) {
        Map<String, String> request = HelperService.parseRequest(message.getText());

        if (request.size() == 1) {
            return sendMessageService.getSendMessage(message, "Введите данные в виде:\n" +
                    Commands.SET_FREE_TIME + "\n" +
                    "userName: Donald Trump\n" +
                    "provider: BarberShop\n" +
                    "date: 12.07.2021");
        }
        if(!request.containsKey("userName")){
            SendMessage result = new SendMessage();
            result.setText("Укажите userName!");
            result.setChatId(String.valueOf(message.getChatId()));
            return result;
        }
        User user = userRepository.findByName(request.get("userName"));
        Provider myProvider = new Provider();

        if(user.getProviders().isEmpty()){
            SendMessage result = new SendMessage();
            result.setText("Этот пользователь не предоставляет услуги!");
            result.setChatId(String.valueOf(message.getChatId()));
            return result;
        }

        if(user.getProviders().size() == 1){
            myProvider = user.getProviders().get(0);
        }

        for (Provider provider: user.getProviders()) {
            if(provider.getName().equals(request.get("provider"))){
                myProvider = provider;
            }
        }

        if(myProvider.getId() == null){
            SendMessage result = new SendMessage();
            result.setText("У этого пользователя нету услуги с таким именем!");
            result.setChatId(String.valueOf(message.getChatId()));
            return result;
        }

        final long providerId = myProvider.getId();
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        if(request.containsKey("date")){
            date = request.get("date");
        }
        LocalDate dateFromString = TimeUtil.getDateFromString(date);

        if (providerRepository.findById(providerId) == null) {
            return sendMessageService.getSendMessage(message, "There is no provider with that id: " + providerId + "!");
        }
        if (scheduleRepository.findAllByProvider(providerId).isEmpty()) {
            return sendMessageService.getSendMessage(message, "This provider does not provide any services!");
        }
        List<Session> sessionByProvider = sessionRepository.findByProviderAndDate(providerId, dateFromString);
        if (sessionByProvider.isEmpty()) {

            List<Session> sessions = getSessions(providerId, dateFromString);
            if (sessions.isEmpty()) {
                return sendMessageService.getSendMessage(message, "The provider has no schedule for this day: " + date + "!");
            }
            sessionRepository.saveAll(sessions);
        }

        List<Session> sessionByProviderAndDate = sessionRepository.findByProviderAndDate(providerId, dateFromString);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonList = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        int iconInRow = 3;
        for (Session session : sessionByProviderAndDate) {
            if (session.getCustomer() == null) {
                String time = TimeUtil.getStringFromTime(
                        TimeUtil.getDateTimeFromUtcDateTimeForZone(
                                session.getSessionStart(),
                                userRepository.findByTelegramId(message.getFrom().getId()).getZoneId()).toLocalTime());
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(time);
                button.setCallbackData(ButtonCommands.SET_FREE_TIME + HelperService.PARAM_DIVIDER + time +
                        HelperService.PARAM_DIVIDER + providerId + HelperService.PARAM_DIVIDER + date);
                keyboardButtonList.add(button);
            }
            if(keyboardButtonList.size() == iconInRow){
                rowList.add(keyboardButtonList);
                keyboardButtonList = new ArrayList<>();
            }
        }
        InlineKeyboardButton buttonCancel = new InlineKeyboardButton();
        buttonCancel.setText("Отмена!");
        buttonCancel.setCallbackData(ButtonCommands.SET_FREE_TIME + HelperService.PARAM_DIVIDER + ButtonCommands.CANCEL);
        keyboardButtonList.add(buttonCancel);
        rowList.add(keyboardButtonList);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText("Выберите свободное время!");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
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
}
