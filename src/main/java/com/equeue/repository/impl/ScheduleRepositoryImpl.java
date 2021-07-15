package com.equeue.repository.impl;

import com.equeue.entity.Schedule;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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



}
