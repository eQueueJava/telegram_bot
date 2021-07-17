package com.equeue.entity;

import com.equeue.service.TimeUtil;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class User {

    private Long id;
    private String name;
    private String role;
    private Long telegramId;
    private ZoneId zoneId;
    private List<Provider> providers = new ArrayList<>();
    private List<Session> sessions = new ArrayList<>();

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getRole() {
        return this.role;
    }

    public Long getTelegramId() {
        return this.telegramId;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public User setRole(String role) {
        this.role = role;
        return this;
    }

    public User setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public User setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
        return this;
    }

    public User setProviders(List<Provider> providers) {
        this.providers = providers;
        return this;
    }

    public User setSessions(List<Session> sessions) {
        this.sessions = sessions;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(role, user.role) && Objects.equals(telegramId, user.telegramId) && Objects.equals(providers, user.providers) && Objects.equals(sessions, user.sessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, role, telegramId, providers, sessions);
    }

    @Override
    public String toString() {
        return "User { " +
                "id=" + id + ", \n" +
                "name='" + name + "', \n" +
                "role='" + role + "', \n" +
                "telegramId=" + telegramId + ", \n" +
                "timeZone=" + zoneId +  ", \n" +
                "providers=" + "\n\t" +
                (providers == null || providers.isEmpty() ? "none" : providers.stream()
                        .map(p -> "'" + p.getName() + "' (" + p.getId() + ")")
                        .collect(Collectors.joining(", \n\t"))) + ", \n" +
                "sessions=" + "\n\t" +
                (sessions == null || sessions.isEmpty() ? "none" : sessions.stream()
                        .map(s -> s.getProvider().getId() + ": " +
                                TimeUtil.stringFromLocalDateTime(s.getSessionStart())
                        )
                        .collect(Collectors.joining(", \n\t"))) +
                " }";
    }

}
