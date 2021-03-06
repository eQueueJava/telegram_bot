package com.equeue.entity;

import com.equeue.service.HelperService;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.util.Objects;

public class Schedule {

    private Provider provider;
    private Integer dayOfWeek;
    private Timestamp workStart;
    private Timestamp workFinish;
    private Integer duration;

    public Schedule() {
    }

    public Provider getProvider() {
        return provider;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public Timestamp getWorkStart() {
        return workStart;
    }

    public Timestamp getWorkFinish() {
        return workFinish;
    }

    public Integer getDuration() {
        return duration;
    }

    public Schedule setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public Schedule setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public Schedule setWorkStart(Timestamp workStart) {
        this.workStart = workStart;
        return this;
    }

    public Schedule setWorkFinish(Timestamp workFinish) {
        this.workFinish = workFinish;
        return this;
    }

    public Schedule setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(provider, schedule.provider) && Objects.equals(dayOfWeek, schedule.dayOfWeek) && Objects.equals(workStart, schedule.workStart) && Objects.equals(workFinish, schedule.workFinish) && Objects.equals(duration, schedule.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, dayOfWeek, workStart, workFinish, duration);
    }

    @Override
    public String toString() {
        return "Schedule {" + " \n" +
                "provider=" + provider.getName() + "' (" + provider.getId() + ")" + ", \n" +
                "dayOfWeek=" + DayOfWeek.of(dayOfWeek) + ", \n" +
                "workStart=" + HelperService.timeOf(workStart) + ", \n" +
                "workFinish=" + HelperService.timeOf(workFinish) + ", \n" +
                "duration=" + duration +
                " }";
    }

}
