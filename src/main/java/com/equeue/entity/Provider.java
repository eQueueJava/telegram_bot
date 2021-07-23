package com.equeue.entity;

import com.equeue.service.TimeUtil;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Provider {

    private Long id;
    private User client;
    private String name;
    private Map<Integer, Schedule> scheduleMap = new TreeMap<>();

    public Long getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Schedule> getScheduleMap() {
        return scheduleMap;
    }

    public Provider setId(Long id) {
        this.id = id;
        return this;
    }

    public Provider setClient(User client) {
        this.client = client;
        return this;
    }

    public Provider setName(String name) {
        this.name = name;
        return this;
    }

    public Provider setScheduleMap(Map<Integer, Schedule> scheduleMap) {
        this.scheduleMap = scheduleMap;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provider provider = (Provider) o;
        return Objects.equals(id, provider.id) && Objects.equals(client, provider.client) && Objects.equals(name, provider.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, name);
    }

    @Override
    public String toString() {
        return "Provider { " +
                "id=" + id + ", \n" +
                "name='" + name + "'" + ", \n" +
                "client='" + client.getName() + "' (" + client.getId() + ")" + ", \n" +
                "schedules=" + " \n\t" +
                (scheduleMap == null || scheduleMap.size() == 0 ? "none" : scheduleMap.values().stream()
                        .map(s -> DayOfWeek.of(s.getDayOfWeek()) + ":\t" +
                                TimeUtil.getStringFromTime(
                                        TimeUtil.getTimeFromUtcTimeForZone(s.getWorkStart(), client.getZoneId())) + "-" +
                                TimeUtil.getStringFromTime(
                                        TimeUtil.getTimeFromUtcTimeForZone(s.getWorkFinish(), client.getZoneId())) +
                                " (" + s.getDuration() + "min)")
                        .collect(Collectors.joining(", \n\t"))) +
                " }";
    }

}
