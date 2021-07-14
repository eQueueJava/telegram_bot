package com.equeue.service;

import com.equeue.entity.Session;
import com.equeue.repository.ProviderRepository;
import com.equeue.repository.SessionRepository;
import com.equeue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    UserRepository userRepository;

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public Session findById(Long id) {
        return sessionRepository.findById(id);
    }

}
