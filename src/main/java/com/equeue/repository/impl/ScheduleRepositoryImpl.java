package com.equeue.repository.impl;

import com.equeue.entity.Schedule;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import com.equeue.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    @Autowired
    ProviderRepository providerRepository;

    Map<Long, Map<Integer, Schedule>> scheduleMap = new HashMap<>();

    @Override
    public Schedule save(Schedule schedule) {
        final Long providerId = schedule.getProvider().getId();
        final Integer dayOfWeek = schedule.getDayOfWeek();
        scheduleMap.computeIfAbsent(providerId, k -> new TreeMap<>());
        Map<Integer, Schedule> providerScheduleMap = scheduleMap.get(providerId);
        providerScheduleMap.put(dayOfWeek, schedule);
        providerRepository.findById(providerId).setScheduleMap(providerScheduleMap);
        return providerScheduleMap.get(dayOfWeek);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleMap.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
    }

    public boolean hasProviderAnySchedules(Long providerId) {
        return scheduleMap.containsKey(providerId);
    }

    public boolean hasProviderScheduleSpecificDay(Long providerId, String date) {
        int deyFromDate = 0;
        try {
            deyFromDate = HelperService.getDeyFromDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<Integer, Schedule> integerScheduleMap = scheduleMap.get(providerId);
        Schedule schedule = integerScheduleMap.get(deyFromDate);
        return schedule != null;
    }

    public Schedule getScheduleOfCurrentDay(Long providerId, String date){
        int deyFromDate = 0;
        try {
            deyFromDate = HelperService.getDeyFromDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<Integer, Schedule> integerScheduleMap = scheduleMap.get(providerId);
        return integerScheduleMap.get(deyFromDate);
    }

    public List<String> generateTemplate(Schedule schedule) {
        String startTime = HelperService.timeOf(schedule.getWorkStart());
        String finishTime = HelperService.timeOf(schedule.getWorkFinish());
        double duration = schedule.getDuration() / 60.0;
        double start = HelperService.changeStrTimeToDouble(startTime);
        double finish = HelperService.changeStrTimeToDouble(finishTime);

        List<String> list = new ArrayList<>();
        list.add(start + "0");
        for (double i = start; i < finish; i = i + duration) {
            list.add(generateTime(i, duration));
        }
        list = HelperService.formList(list);
        return list;
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
