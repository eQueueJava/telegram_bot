package com.equeue.telegram_bot.repository_test;

import com.equeue.entity.Provider;
import com.equeue.entity.Schedule;
import com.equeue.entity.User;
import com.equeue.entity.enumeration.UserRole;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import com.equeue.repository.impl.ScheduleRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class ScheduleRepositoryTests {
    @Mock
    private ProviderRepository providerRepository;
    ScheduleRepository scheduleRepository;

    @BeforeEach
    void setUp(){
        scheduleRepository = new ScheduleRepositoryImpl();
    }

    @Test
    void deleteByProvider(){
        Schedule schedule1 = getSchedule1();

        Schedule schedule2 = getSchedule2();

        Map<Long, Map<Integer, Schedule>> scheduleMap = new HashMap<>();
        Map<Integer, Schedule> scheduleMap1 = new HashMap<>();
        scheduleMap1.put(1, schedule1);
        Map<Integer, Schedule> scheduleMap2 = new HashMap<>();
        scheduleMap2.put(2, schedule2);
        scheduleMap.put(1L, scheduleMap1);
        scheduleMap.put(2L, scheduleMap2);
        ScheduleRepositoryImpl.setScheduleMap(scheduleMap);

        Map<Integer, Schedule> actual = scheduleRepository.deleteByProvider(schedule1.getProvider());
        Map<Integer, Schedule> expected = scheduleMap1;

        assertEquals(expected, actual);
    }

    private Schedule getSchedule1() {
        Schedule schedule1 = new Schedule();
        Provider provider1 = new Provider().setId(1L).setClient(
                new User().setId(1L).setName("TestUser1").setUserRole(UserRole.CLIENT).setTelegramId(1111111111L));
        schedule1.setProvider(provider1);
        schedule1.setDayOfWeek(1);
        schedule1.setWorkStart(LocalTime.of(6, 0));
        schedule1.setWorkFinish(LocalTime.of(14, 0));
        schedule1.setDuration(30);
        return schedule1;
    }

    private Schedule getSchedule2() {
        Schedule schedule2 = new Schedule();
        Provider provider2 = new Provider().setId(2L).setClient(
                new User().setId(2L).setName("TestUser2").setUserRole(UserRole.CLIENT).setTelegramId(22222222L));
        schedule2.setProvider(provider2);
        schedule2.setDayOfWeek(1);
        schedule2.setWorkStart(LocalTime.of(10, 0));
        schedule2.setWorkFinish(LocalTime.of(14, 0));
        schedule2.setDuration(30);
        return schedule2;
    }
}
