package com.equeue.repository;

import com.equeue.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {

    Schedule save(Schedule schedule);

    List<Schedule> findAll();

    boolean hasProviderAnySchedules(Long providerId);

    boolean hasProviderScheduleSpecificDay(Long providerId, String date);

    Schedule getScheduleOfCurrentDay(Long providerId, String date);

    List<String> generateTemplate(Schedule schedule);

}
