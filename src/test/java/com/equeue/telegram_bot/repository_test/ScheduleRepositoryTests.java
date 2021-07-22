package com.equeue.telegram_bot.repository_test;

import com.equeue.entity.Provider;
import com.equeue.entity.Schedule;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.impl.ScheduleRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class ScheduleRepositoryTests {
    @Mock
    private ProviderRepository providerRepository;
    ScheduleRepositoryImpl scheduleRepository;

    @BeforeEach
    void setUp(){
        scheduleRepository = new ScheduleRepositoryImpl(providerRepository);
    }

    @Test
    void deleteAllForProvider(){
//        Provider provider = new Provider();
//        provider.setId(1L);
//        Map<Long, Map<Integer, Schedule>> scheduleMap = new HashMap<>();
//        Map<Integer, Schedule> session1 = new HashMap<>();
//        Schedule schedule1 = new Schedule().setProvider(provider);
//        session1.put(1, schedule1);
//        Schedule schedule2 = new Schedule().setProvider(provider);
//        session1.put(2, schedule2);
//        Map<Integer, Schedule> session2 = new HashMap<>();
//        session2.put(1, new Schedule());
//        session2.put(2, new Schedule());
//        scheduleMap.put(1L,session1);
//        scheduleMap.put(2L,session2);
//        ScheduleRepositoryImpl.setScheduleMap(scheduleMap);
//
//        List<Schedule> expected = new ArrayList<>();
//        expected.add(schedule1);
//        expected.add(schedule2);
//
////        given(scheduleRepository.deleteAllForProvider(provider)).willReturn(expected);
//        List<Schedule> actual = scheduleRepository.deleteAllForProvider(provider);
//        System.out.println("s");
//        assertEquals(expected, actual);
    }
}
