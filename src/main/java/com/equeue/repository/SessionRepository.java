package com.equeue.repository;

import com.equeue.entity.Provider;
import com.equeue.entity.Session;

import java.time.LocalDate;
import java.util.List;

public interface SessionRepository {

    Session save(Session session);

    List<Session> findAll();

    Session findById(Long id);

    List<Session> findByProvider(Long providerId);

    List<Session> findByProviderAndDate(Long providerId, LocalDate date);

    List<Session> deleteAllForProvider(Provider provider);

    Session deleteById(Long id);

    List<Session> saveAll(List<Session> sessions);
}
