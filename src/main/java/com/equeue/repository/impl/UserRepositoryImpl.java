package com.equeue.repository.impl;

import com.equeue.entity.User;
import org.springframework.stereotype.Repository;
import com.equeue.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final Map<Long, User> USER_MAP = new HashMap<>();
    Long id = 1L;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(id++);
        }
        USER_MAP.put(user.getId(), user);
        return USER_MAP.get(user.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(USER_MAP.values());
    }

    @Override
    public User findById(Long id) {
        return USER_MAP.get(id);
    }

    @Override
    public User findByName(String name) {
        for (Map.Entry<Long, User> entry: USER_MAP.entrySet()) {
            if(entry.getValue().getName().equals(name)){
                return entry.getValue();
            }
        }
        return new User();
    }

    @Override
    public User findByTelegramId(Long id) {
        for (Map.Entry<Long, User> entry: USER_MAP.entrySet()) {
            if(entry.getValue().getTelegramId().equals(id)){
                return entry.getValue();
            }
        }
        return null;
    }
}
