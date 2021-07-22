package com.equeue.repository;

import com.equeue.entity.Provider;
import com.equeue.entity.Schedule;

import java.util.List;
import java.util.Map;

public interface ScheduleRepository {

    Schedule save(Schedule schedule);

    List<Schedule> findAll();

    boolean hasProviderAnySchedules(Long providerId);

    boolean hasProviderScheduleSpecificDay(Long providerId, String date);

    Schedule getScheduleOfCurrentDay(Long providerId, String date);

    List<String> generateTemplate(Schedule schedule);

    List<Schedule> deleteAllForProvider(Provider provider);
}
