package com.equeue.entity;

import com.equeue.entity.enumeration.UserRole;
import com.equeue.service.TimeUtil;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class User {

    private Long id;
    private String name;
    private UserRole userRole;
    private Long telegramId;
    private String telegramUsername;
    private ZoneId zoneId;
    private List<Provider> providers = new ArrayList<>();
    private List<Session> sessions = new ArrayList<>();

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public UserRole getUserRole() {
        return this.userRole;
    }

    public Long getTelegramId() {
        return this.telegramId;
    }

    public String getTelegramUsername() {
        return telegramUsername;
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

    public User setUserRole(UserRole userRole) {
        this.userRole = userRole;
        return this;
    }

    public User setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public User setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
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
        return Objects.equals(id, user.id) && Objects.equals(name, user.name)
                && Objects.equals(userRole, user.userRole) && Objects.equals(telegramId, user.telegramId)
                && Objects.equals(providers, user.providers) && Objects.equals(sessions, user.sessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userRole, telegramId, providers, sessions);
    }

    @Override
    public String toString() {
        String idText = "Ваш ID - " + id + " \n";
        String nameText = "Ваше имя - '" + name + "' \n";
        String userRoleText = "Вы зарегистрированы как - '" + userRole + "' \n";
        String timeZone = "Ваш часовой пояс - " + zoneId + " \n";

        String result = idText + nameText + userRoleText + timeZone;

        if (!providers.isEmpty()) {
            result += "Ваши заведения : " + providers.stream()
                    .map(p -> "'" + p.getName() + "' ( Id заведения - " + p.getId() + ")")
                    .collect(Collectors.joining(", \n\t")) + ", \n";
        }

        if (!sessions.isEmpty()) {
            result += "К вам записаны : " + sessions.stream()
                    .map(s -> s.getProvider().getName() + ": " +
                            TimeUtil.getStringFromDateTime(s.getSessionStart())
                    )
                    .collect(Collectors.joining(" \n\t"));
        }

        return result;
    }
}
