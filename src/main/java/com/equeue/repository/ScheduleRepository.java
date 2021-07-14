package com.equeue.repository;

import com.equeue.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {

    Schedule save(Schedule schedule);

    List<Schedule> findAll();

}
