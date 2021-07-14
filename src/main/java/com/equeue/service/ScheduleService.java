package com.equeue.service;

import com.equeue.entity.Schedule;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import com.equeue.telegram_bot.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.ParseException;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ProviderRepository providerRepository;

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public String save(Message message) {
        String text = message.getText();
        if (text.replace(Commands.CREATE_SCHEDULE, "").isBlank()) {
            return "Введите данные в виде:\n" +
                    Commands.CREATE_SCHEDULE + "\n" +
                    "provider: 1\n" +
                    "dayOfWeek: 1\n" +
                    "workStart: 9:00\n" +
                    "workFinish: 18:00\n" +
                    "duration: 30";
        }

        String[] lines = text.split("\n");
        Schedule schedule = new Schedule();
        try {
            schedule
                    .setProvider(providerRepository.findById(Long.valueOf(lines[1].replace("provider:", "").trim())))
                    .setDayOfWeek(Integer.valueOf(lines[2].replace("dayOfWeek:", "").trim()))
                    .setWorkStart(HelperService.timeOf(lines[3].replace("workStart:", "").trim()))
                    .setWorkFinish(HelperService.timeOf(lines[4].replace("workFinish:", "").trim()))
                    .setDuration(Integer.valueOf(lines[5].replace("duration:", "").trim()));
            save(schedule);
            return schedule.toString();
        } catch (ParseException e) {
            return e.getMessage();//TODO
        }
    }


}
