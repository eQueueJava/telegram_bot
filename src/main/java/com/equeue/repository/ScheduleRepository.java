package com.equeue.repository;

import com.equeue.entity.Schedule;

import java.util.Collection;
import java.util.List;

public interface ScheduleRepository {

    Schedule save(Schedule schedule);

    List<Schedule> findAll();

    Collection<Schedule> findAllByProvider(Long providerId);

    Schedule findByProviderAndDayOfWeek(Long providerId, Integer value);

}
