package com.equeue.repository.impl;

import com.equeue.entity.Session;
import org.springframework.stereotype.Repository;
import com.equeue.repository.SessionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SessionRepositoryImpl implements SessionRepository {

    Map<Long, Session> sessionMap = new HashMap<>();
    Long id = 1L;

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

}
