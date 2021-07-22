package com.equeue.repository.impl;

import com.equeue.entity.Provider;
import com.equeue.entity.Schedule;
import com.equeue.entity.User;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import com.equeue.service.HelperService;
import com.equeue.service.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    @Autowired
    ProviderRepository providerRepository;

    //long->provider
    static final Map<Long, Map<Integer, Schedule>> SCHEDULE_MAP = new HashMap<>();

    @Override
    public Schedule save(Schedule schedule) {
        final Long providerId = schedule.getProvider().getId();
        final Integer dayOfWeek = schedule.getDayOfWeek();
        SCHEDULE_MAP.computeIfAbsent(providerId, k -> new TreeMap<>());
        Map<Integer, Schedule> providerScheduleMap = SCHEDULE_MAP.get(providerId);
        providerScheduleMap.put(dayOfWeek, schedule);
        providerRepository.findById(providerId).setScheduleMap(providerScheduleMap);
        return providerScheduleMap.get(dayOfWeek);
    }

    @Override
    public List<Schedule> findAll() {
        return SCHEDULE_MAP.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
    }

    public boolean hasProviderAnySchedules(Long providerId) {
        return SCHEDULE_MAP.containsKey(providerId);
    }

    public boolean hasProviderScheduleSpecificDay(Long providerId, String date) {
        int dayFromDate = TimeUtil.localDateFromString(date).getDayOfWeek().getValue();
        Map<Integer, Schedule> integerScheduleMap = SCHEDULE_MAP.get(providerId);
        Schedule schedule = integerScheduleMap.get(dayFromDate);
        return schedule != null;
    }

    public Schedule getScheduleOfCurrentDay(Long providerId, String date){
        int dayFromDate = TimeUtil.localDateFromString(date).getDayOfWeek().getValue();
        Map<Integer, Schedule> integerScheduleMap = SCHEDULE_MAP.get(providerId);
        return integerScheduleMap.get(dayFromDate);
    }

    public List<String> generateTemplate(Schedule schedule) {
        String startTime = TimeUtil.stringFromLocalTime(schedule.getWorkStart());
        String finishTime = TimeUtil.stringFromLocalTime(schedule.getWorkFinish());
        double duration = schedule.getDuration() / 60.0;
        double start = TimeUtil.changeStrTimeToDouble(startTime);
        double finish = TimeUtil.changeStrTimeToDouble(finishTime);

        List<String> list = new ArrayList<>();
        list.add(start + "0");
        for (double i = start; i < finish; i = i + duration) {
            list.add(generateTime(i, duration));
        }
        list = HelperService.formList(list);
        return list;
    }

    @Override
    public List<Schedule> deleteAllForProvider(Provider provider) {
        List<Schedule> schedules = new ArrayList<>();
        //test//todo
        for (Map.Entry<Long, Map<Integer, Schedule>> entry: SCHEDULE_MAP.entrySet()) {
            for (Map.Entry<Integer, Schedule> secondEntry: entry.getValue().entrySet()) {
                if(secondEntry.getValue().getProvider().equals(provider)){
                    schedules.add(secondEntry.getValue());
                    SCHEDULE_MAP.remove(entry.getKey());
                }//todo
            }
        }
        return schedules;
    }

    private String generateTime(double i, double duration) {
        String temp = i + "";
        String finish = "";
        double currentTime = i + duration;
        String currentTimeStr = currentTime + "";
        if (temp.contains(".") && temp.charAt(temp.length() - 1) != '0') {
            for (int j = 1; j < currentTimeStr.length(); j++) {
                if (currentTimeStr.charAt(j - 1) == '.') {
                    finish += currentTimeStr.charAt(j);
                }
            }
            int res = Integer.parseInt(finish);
            double i1 = (res / 10.0) * 60;
            return (int) currentTime + ":" + (int) i1 + "0";
        } else {
            return (int) i + ":" + (int) (60 * duration) + "";
        }
    }
}
