package com.equeue.repository;

import com.equeue.entity.Session;

import java.util.List;

public interface SessionRepository {

    Session save(Session session);

    List<Session> findAll();

    Session findById(Long id);

}
