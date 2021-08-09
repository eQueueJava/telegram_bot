package com.equeue.service;

import com.equeue.entity.Provider;
import com.equeue.entity.Schedule;
import com.equeue.entity.Session;
import com.equeue.entity.User;
import com.equeue.entity.enumeration.UserRole;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.ScheduleRepository;
import com.equeue.repository.UserRepository;
import com.equeue.telegram_bot.Commands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    ScheduleRepository scheduleRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ProviderRepository providerRepository;
    @InjectMocks
    ScheduleService scheduleService;

    @Test
    void save() {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(1L);
        org.telegram.telegrambots.meta.api.objects.User userTg = new org.telegram.telegrambots.meta.api.objects.User();
        userTg.setId(1L);
        message.setFrom(userTg);
        message.setChat(chat);

        message.setText(Commands.CREATE_SCHEDULE);
        Assertions.assertEquals("Введите данные в виде:\n" +
                        Commands.CREATE_SCHEDULE + "\n" +
                        "provider: BarberShop\n" +
                        "dayOfWeek: 1\n" +
                        "workStart: 9:00\n" +
                        "workFinish: 18:00\n" +
                        "duration: 30",
                scheduleService.save(message));

//        message.setText(Commands.CREATE_SCHEDULE + "\n" +
//                "provider: BarberShop\n" +
//                "dayOfWeek: 1\n" +
//                "workStart: 9:00\n" +
//                "workFinish: 18:00\n" +
//                "duration: 30");
//        Map<Integer, Schedule> scheduleMap = new TreeMap<>();
//        scheduleMap.put(1, new Schedule());
//        ArrayList<Provider> arrayList = new ArrayList<>();
//        arrayList.add(new Provider().setId(1L).setClient(new User()).setName("BarberShop").setScheduleMap(scheduleMap));
//        when(userRepository.findByTelegramId(anyLong())).thenReturn(new User().setProviders(arrayList).setZoneId(ZoneId.of("Europe/Kiev")));
//        when(scheduleRepository.save(any())).thenReturn(any());
//        Assertions.assertEquals("Расписание для - BarberShop\nДень недели - MONDAY\nНачало работы - 9:00\n" +
//                "Конец работы - 18:00\nПродолжительность услуги - 30 минут",
//                scheduleService.save(message));
    }

    @Test
    void failSave() {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(1L);
        message.setChat(chat);
        message.setText(Commands.CREATE_SCHEDULE + "\n" +
                "provider: BarberShop\n" +
                "dayOfWeek: 1\n" +
                "workStart: 9:00\n" +
                "workFinish: 18:00\n" +
                "duration: 30");
        when(userRepository.findByTelegramId(anyLong())).thenReturn(new User().setProviders(new ArrayList<>()));
        Assertions.assertEquals("У вас нету предоставителя услуг!", scheduleService.save(message));


    }

//    private User getUser(Long id){
//        User user = new User();
//        user.setId(id);
//        user.setName("TestUser" + id);
//        user.setUserRole(UserRole.CLIENT);
//        user.setTelegramId(id);
//        user.setTelegramUsername("TgName" + id);
//        user.setZoneId(ZoneId.of("Europe/Kiev"));
//        user.setProviders(getProviders());
//        user.setSessions();
//        return user;
//    }
//
//    private List<Provider> getProviders(){
//        List<Provider> providers = new ArrayList<>();
//        providers.add(getProvider(1L));
//        return providers;
//    }
//
//    private Provider getProvider(Long id){
//        Provider provider = new Provider();
//        provider.setId(id);
//        provider.setName("TestProvider" + id);
//        provider.setScheduleMap(getScheduleMap(1, provider));
//        return provider;
//    }
//
//    private  Map<Integer, Schedule> getScheduleMap(Integer key, Provider provider){
//        Map<Integer, Schedule> scheduleMap = new TreeMap<>();
//        scheduleMap.put(key, getSchedule(provider));
//        return scheduleMap;
//    }
//
//    private Schedule getSchedule(Provider provider){
//        Schedule schedule = new Schedule();
//        schedule.setProvider(provider);
//        schedule.setDayOfWeek(1);
//        schedule.setWorkStart(TimeUtil.getTimeFromString(("09:00".trim())));
//        schedule.setWorkFinish(TimeUtil.getTimeFromString(("18:00".trim())));
//        schedule.setDuration(30);
//        return schedule;
//    }
//
//    private List<Session> getSessions(){
//        List<Session> sessions = new ArrayList<>();
//        sessions.add();
//        return sessions;
//    }
//
//    private Session getSession(Long id){
//        Session session = new Session();
//        session.setId(id);
//        return session;
//    }
}