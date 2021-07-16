package com.equeue.repository.impl;

import com.equeue.entity.Session;
import com.equeue.service.HelperService;
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

    public Map<Long, Session> findSessionByProvider(long providerId) {
        Map<Long, Session> res = new HashMap<>();
        for (Map.Entry<Long, Session> entry : sessionMap.entrySet()) {
            Long id = entry.getKey();
            Session session = sessionMap.get(id);
            Long provId = session.getProvider().getId();
            if (provId == providerId) {
                res.put(id, session);
            }
        }
        return res;
    }

    public Map<Long, Session> findSessionByProviderOfDate(long providerId, String date) {
        Map<Long, Session> res = new HashMap<>();
        Map<Long, Session> sessionByProv = findSessionByProvider(providerId);
        if (!sessionByProv.isEmpty()){
            for (Map.Entry<Long, Session> entry : sessionMap.entrySet()) {
                Long key = entry.getKey();
                Session session = sessionByProv.get(key);
                String trim = HelperService.dateOf(session.getSessionStart()).trim();
                if (date.equals(trim)){
                    res.put(key, session);
                }
            }
        }
        return res;
    }
}
