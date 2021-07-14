package com.equeue.repository.impl;

import com.equeue.entity.Schedule;
import org.springframework.stereotype.Repository;
import com.equeue.repository.ScheduleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    Map<Long, Map<Integer, Schedule>> scheduleMap = new HashMap<>();

    @Override
    public Schedule save(Schedule schedule) {
        final Long userId = schedule.getProvider().getId();
        final Integer dayOfWeek = schedule.getDayOfWeek();
        scheduleMap.computeIfAbsent(userId, k -> new HashMap<>());
        scheduleMap.get(userId).put(dayOfWeek, schedule);
        return scheduleMap.get(userId).get(dayOfWeek);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleMap.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
    }



}
