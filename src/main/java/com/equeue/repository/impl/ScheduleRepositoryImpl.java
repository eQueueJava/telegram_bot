package com.equeue.repository.impl;

import com.equeue.entity.Provider;
import com.equeue.entity.Schedule;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    @Autowired
    ProviderRepository providerRepository;

    //long->providerId
    private static Map<Long, Map<Integer, Schedule>> scheduleMap = new ConcurrentHashMap<>();

    public static void setScheduleMap(Map<Long, Map<Integer, Schedule>> scheduleMap) {
        ScheduleRepositoryImpl.scheduleMap = scheduleMap;
    }

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

    @Override
    public Collection<Schedule> findAllByProvider(Long providerId) {
        return scheduleMap.get(providerId).values();
    }

    @Override
    public Schedule findByProviderAndDayOfWeek(Long providerId, Integer dayOfWeek) {
        return scheduleMap.get(providerId).get(dayOfWeek);
    }

    @Override
    public Map<Integer, Schedule> deleteByProvider(Provider provider) {
        return scheduleMap.remove(provider.getId());
    }

}
