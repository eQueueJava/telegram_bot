package com.equeue.repository;

import com.equeue.entity.Provider;
import com.equeue.entity.Session;

import java.util.List;
import java.util.Map;

public interface SessionRepository {

    Session save(Session session);

    List<Session> findAll();

    Session findById(Long id);

    Map<Long, Session> findSessionByProvider(long providerId);

    Map<Long, Session> findSessionByProviderOfDate(long providerId, String date);

    List<Session> deleteAllForProvider(Provider provider);

    Session deleteById(Long id);
}
