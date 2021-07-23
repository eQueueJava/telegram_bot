package com.equeue.repository.impl;

import com.equeue.entity.Provider;
import com.equeue.entity.Session;
import com.equeue.repository.SessionRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SessionRepositoryImpl implements SessionRepository {

    private static final Map<Long, Session> sessionMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public Session save(Session session) {
        if (session.getId() == null) {
            session.setId(id++);
        }
        sessionMap.put(session.getId(), session);
        return sessionMap.get(session.getId());
    }

    @Override
    public List<Session> findAll() {
        return new ArrayList<>(sessionMap.values());
    }

    @Override
    public Session findById(Long id) {
        return sessionMap.get(id);
    }

    public List<Session> findByProvider(Long providerId) {
        return sessionMap.values().stream()
                .filter(s -> s.getProvider().getId().equals(providerId))
                .collect(Collectors.toList());
    }

    public List<Session> findByProviderAndDate(Long providerId, LocalDate date) {
        return sessionMap.values().stream()
                .filter(s -> s.getProvider().getId().equals(providerId))
                .filter(s -> s.getSessionStart().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Session> deleteAllForProvider(Provider provider) {
        List<Session> sessionsDeleted = new ArrayList<>();
        List<Session> sessionsForDel = findByProvider(provider.getId());
        for (Session sessionForDel : sessionsForDel) {
            sessionsDeleted.add(deleteById(sessionForDel.getId()));
        }
        return sessionsDeleted;
    }

    @Override
    public Session deleteById(Long id) {
        return sessionMap.remove(id);
    }

}
