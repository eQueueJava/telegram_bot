package com.equeue.repository.impl;

import com.equeue.entity.Provider;
import com.equeue.entity.Session;
import com.equeue.service.TimeUtil;
import org.springframework.stereotype.Repository;
import com.equeue.repository.SessionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SessionRepositoryImpl implements SessionRepository {

    private static final Map<Long, Session> SESSION_MAP = new HashMap<>();
    private Long id = 1L;

    @Override
    public Session save(Session session) {
        if (session.getId() == null) {
            session.setId(id++);
        }
        SESSION_MAP.put(session.getId(), session);
        return SESSION_MAP.get(session.getId());
    }

    @Override
    public List<Session> findAll() {
        return new ArrayList<>(SESSION_MAP.values());
    }

    @Override
    public Session findById(Long id) {
        return SESSION_MAP.get(id);
    }

    public Map<Long, Session> findSessionByProvider(long providerId) {
        Map<Long, Session> res = new HashMap<>();
        for (Map.Entry<Long, Session> entry : SESSION_MAP.entrySet()) {
            Long sessionId = entry.getKey();
            Session session = SESSION_MAP.get(sessionId);
            Long provId = session.getProvider().getId();
            if (provId == providerId) {
                res.put(sessionId, session);
            }
        }
        return res;
    }

    public Map<Long, Session> findSessionByProviderOfDate(long providerId, String date) {
        Map<Long, Session> res = new HashMap<>();
        Map<Long, Session> sessionByProv = findSessionByProvider(providerId);
        if (!sessionByProv.isEmpty()){
            for (Map.Entry<Long, Session> entry : SESSION_MAP.entrySet()) {
                Long key = entry.getKey();
                Session session = sessionByProv.get(key);
                String trim = TimeUtil.stringFromLocalDate(session.getSessionStart().toLocalDate()).trim();
                if (date.equals(trim)){
                    res.put(key, session);
                }
            }
        }
        return res;
    }

    @Override
    public List<Session> deleteAllForProvider(Provider provider) {
        List<Session> sessions = new ArrayList<>();
        Map<Long, Session> sessionMap = findSessionByProvider(provider.getId());
        for (Map.Entry<Long, Session> entry: sessionMap.entrySet()) {
            sessions.add(deleteById(entry.getKey()));
        }
        return sessions;
    }

    @Override
    public Session deleteById(Long id) {
        return SESSION_MAP.remove(id);
    }
}
