package com.equeue.service;

import com.equeue.entity.Provider;
import com.equeue.entity.Schedule;
import com.equeue.entity.User;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProviderRepository providerRepository;

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public String save(Message message) {
        Map<String, String> request = HelperService.parseRequest(message.getText());
        if (request.size() == 1) {
            return "Введите данные в виде:\n" +
                    Commands.CREATE_SCHEDULE + "\n" +
                    "provider: BarberShop\n" +
                    "dayOfWeek: 1\n" +
                    "workStart: 9:00\n" +
                    "workFinish: 18:00\n" +
                    "duration: 30";
        }
        Provider provider;
        User user = userRepository.findByTelegramId(message.getChatId());
        if(user.getProviders().isEmpty()){
            return "У вас нету предоставителя услуг!";
        } else if (user.getProviders().size() == 1){
            provider = user.getProviders().get(0);
        }else if (user.getProviders().size() > 1 && !request.containsKey("provider")){
            return "Укажите (provider:)!";
        }else{
            provider = providerRepository.findByName(request.get("provider"));
        }
        Schedule schedule = new Schedule();
        schedule.setProvider(provider)
                .setDayOfWeek(Integer.valueOf((request.get("dayOfWeek").trim())))
                .setWorkStart(TimeUtil.getUtcTimeFromTimeAndZone(
                        TimeUtil.getTimeFromString((request.get("workStart").trim())),
                        userRepository.findByTelegramId(message.getFrom().getId()).getZoneId()))
                .setWorkFinish(TimeUtil.getUtcTimeFromTimeAndZone(
                        TimeUtil.getTimeFromString((request.get("workFinish").trim())),
                        userRepository.findByTelegramId(message.getFrom().getId()).getZoneId()))
                .setDuration(Integer.valueOf((request.get("duration").trim())));
        save(schedule);
        return schedule.toString();
    }
}
